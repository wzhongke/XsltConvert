''' the proxy program '''
#!/usr/bin/env python
#-*- coding: gb2312 -*-

import SocketServer
import socket
import select
import re
import os.path
import sys
import time

def parse_cli_params():
    '''parse param form command line'''
    from optparse import OptionParser
    parser = OptionParser()
    parser.add_option("-n", "--name", dest="name", type='string',
                      help="the name registerd in PROXY", metavar='cache')
    parser.add_option("-b", "--bind", dest="bind", type='string',
                      metavar="tcp://127.0.0.1:8881",
                      help="bind this server address")
    parser.add_option("-s", "--server", dest="server", type='string',
                      metavar="tcp://127.0.0.1:11211",
                      help="interactive with this server")

    (options, sys.argv[1:]) = parser.parse_args()
    return options


def parse_proto(conf):
    ''' parse the proto for server configure'''
    return re.match(r'(tcp|udp)://([\w\.]+):(\d+)', conf).groups()


class Handler:
    request, client_address = None, None
    svr_proto, svr_request, svr_addr = None, None, None
    begin_time = None

    def connect_proxy(self):
        ''' connect to remote proxy '''
        self.begin_time = time.time()
        proto, host, port = parse_proto(PROXY_ADDRESS)
        addr = (host, int(port))
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(addr)
        sock.send(PROXY_NAME + "\r\n")
        self.svr_proto, self.svr_request, self.svr_addr = proto, sock, addr
        print self.ellapse(), "connect to proxy"

    def peer_request(self, sock):
        ''' get correct peer '''
        if sock == self.request:
            return self.svr_request
        else:
            return self.request

    def sock_addr(self, sock):
        ''' get correct peer address '''
        if sock == self.request:
            return self.client_address
        else:
            return self.svr_addr

    def ellapse(self):
        return "%.6f" % (time.time() - self.begin_time)



class TcpHandler(Handler, SocketServer.StreamRequestHandler):
    ''' client handler can handle tcp proto '''

    def handle(self):
        ''' handle each request '''

        self.connect_proxy()

        ins = [self.request, self.svr_request]
        ous = []
        data = {}

        while True:
            ready_ins, ready_ous = select.select(ins, ous, [])[:2]
            for sock in ready_ins:
                newdata = sock.recv(8192)
                print self.ellapse(), 'recv %d bytes from %s' % (len(newdata), self.sock_addr(sock))
                if newdata:
                    peer = self.peer_request(sock)
                    data[peer] = data.get(peer, '') + newdata
                    if peer not in ous:
                        ous.append(peer)
                else:
                    print self.ellapse(), "disconnect from", self.sock_addr(sock)
                    if sock in ins:
                        ins.remove(sock)
                    sock.close()

                    if sock == self.request:
                        return

            for sock in ready_ous:
                # output events always mean we can send some data
                tosend = data[sock]
                nsent = sock.send(tosend)
                print self.ellapse(), 'sent %d bytes to %s' % (nsent, self.sock_addr(sock))
                # remember data still to be sent, if any
                tosend = tosend[nsent:]
                if tosend:
                    data[sock] = tosend
                else:
                    del data[sock]
                    ous.remove(sock)


class UdpHandler(Handler, SocketServer.DatagramRequestHandler):
    ''' proxy handler can handle udp proto '''

    def handle(self):
        ''' handle each request '''
        self.connect_proxy()
        self.svr_request.settimeout(2)

        req = self.rfile.read(8192)
        self.svr_request.send(req)
        ack = self.svr_request.recv(8192)
        self.svr_request.close()
        self.wfile.write(ack)


class ProxyHandler(Handler, SocketServer.StreamRequestHandler):
    ''' proxy handler can handle tcp & udp proto '''

    def connect_server(self, peer):
        ''' connect to remote server '''
        self.begin_time = time.time()
        proto, host, port = parse_proto(peer)
        addr = (host, int(port))
        if 'tcp' == proto:
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            try:
                sock.connect(addr)
            except Exception, exc:
                print addr, exc
                return False
        else:
            sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.svr_proto, self.svr_request, self.svr_addr = proto, sock, addr
        print self.ellapse(), "connect to server", addr
        return True

    def handle(self):
        ''' handle each request '''
        peer = self.server.peer(self.request)
        if not self.connect_server(peer):
            return

        ins = [self.request, self.svr_request]
        ous = []
        data = {}

        while True:
            ready_ins, ready_ous = select.select(ins, ous, [])[:2]
            for sock in ready_ins:
                newdata = sock.recv(8192)
                print self.ellapse(), "%d bytes from %s" % (len(newdata), self.sock_addr(sock))
                if newdata:
                    peer = self.peer_request(sock)
                    data[peer] = data.get(peer, '') + newdata
                    if peer not in ous:
                        ous.append(peer)
                else:
                    print self.ellapse(), "disconnect from", self.sock_addr(sock)
                    if sock in ins:
                        ins.remove(sock)
                    sock.close()

                    if sock == self.request:
                        return

            for sock in ready_ous:
                # output events always mean we can send some data
                tosend = data[sock]
                if not tosend:
                    ous.remove(sock)
                    continue
                if sock == self.svr_request and self.svr_proto == 'udp':
                    sock.sendto(tosend, self.svr_addr)
                    print self.ellapse(), "%d bytes to %s" % (len(tosend), self.sock_addr(sock))
                    del data[sock]
                    ous.remove(sock)
                else:
                    nsent = sock.send(tosend)
                    print self.ellapse(), "%d bytes to %s" % (nsent, self.sock_addr(sock))
                    # remember data still to be sent, if any
                    tosend = tosend[nsent:]
                    if tosend:
                        data[sock] = tosend
                    elif self.svr_proto == 'udp':
                        return
                    else:
                        del data[sock]
                        ous.remove(sock)


def parse_config_file():
    ''' parse the config file '''
    fp = file('config.ini')
    cobj = re.compile(r'(\w+)\s*=\s*([^\s]+)')
    config = []
    for ln in fp:
        proxy = ln.strip()
        if not proxy or proxy[0] == '#':
            continue
        config.append(cobj.match(proxy).groups())
    return dict(config)


class ProxyServer(SocketServer.ThreadingTCPServer):
    ''' proxy server must be active as keepalive '''
    allow_reuse_address = True
    proxy_config = {}
    friend = {}

    def peer(self, request):
        ''' get proxy config '''
        return self.friend.get(request)

    def verify_request(self, request, client_address):
        ''' verify the request '''
        name = request.recv(8192).strip()
        peer = self.proxy_config.get(name)
        if peer is None:
            return False
        self.friend[request] = peer
        return True

    def close_request(self, request):
        """Called to clean up an individual request."""
        del self.friend[request]
        SocketServer.ThreadingTCPServer.close_request(self, request)


class TcpClientServer(SocketServer.ThreadingTCPServer):
    ''' client server used to handle tcp proxy '''
    allow_reuse_address = True

class UdpClientServer(SocketServer.ThreadingUDPServer):
    ''' client server used to handle udp proxy '''
    allow_reuse_address = True



if __name__ == '__main__':
    os.chdir(os.path.dirname(os.path.abspath(sys.argv[0])))

    CLI_OPT = parse_cli_params()

    BIND_PROTO, BIND_HOST, BIND_PORT = parse_proto(CLI_OPT.bind)
    BIND_ADDR = (BIND_HOST, int(BIND_PORT))
    if CLI_OPT.bind and not CLI_OPT.server:
        SERVER = ProxyServer(BIND_ADDR, ProxyHandler)
        SERVER.proxy_config = parse_config_file()
    else:
        PROXY_NAME = CLI_OPT.name
        PROXY_ADDRESS = CLI_OPT.server
        if 'tcp' == BIND_PROTO:
            SERVER = TcpClientServer(BIND_ADDR, TcpHandler)
        else:
            SERVER = UdpClientServer(BIND_ADDR, UdpHandler)

    SERVER.serve_forever()

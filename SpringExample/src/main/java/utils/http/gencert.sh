#!/bin/sh
# create self-signed server certificate:
read -p "请输入证书的域 例如[www.example.com or 192.168.1.52]: " DOMAIN
mkdir ca && cd ca  
mkdir newcerts private conf server

SUBJECT="/C=CN/ST=BJ/L=BJ/O=sogou/CN=$DOMAIN"
CA="/C=CN/ST=BJ/L=BJ/O=sogou/CN=sogou"

echo "创建 CA 根证书..."
echo "生成私钥 key 文件..."
openssl genrsa -out private/ca.key 2048  
echo "生成证书请求 csr 文件..."
openssl req -new -subj $CA -key private/ca.key -out private/ca.csr 

echo "生成凭证 crt 文件..."
openssl x509 -req -days 365 -in private/ca.csr -signkey private/ca.key -out private/ca.crt  

echo "为我们的 key 设置起始序列号和创建 CA 键库..."

rm -rf serial index.txt
echo FACE > serial
touch index.txt

echo "服务器证书的生成..."
openssl req -new -subj $SUBJECT -key server/server.key -out server/server.csr  

echo "使用我们私有的 CA key 为刚才的 key 签名..."
openssl ca -in server/server.csr -cert private/ca.crt -keyfile private/ca.key -out server/server.crt -config "./conf/openssl.conf" 

echo "客户端证书的生成 * 创建存放 key 的目录 users..."
mkdir users  
echo " 为用户创建一个 key..."
openssl genrsa -des3 -out ./users/client.key 2048 

echo "为 key 创建一个证书签名请求 csr 文件..."
openssl req -new -key ./users/client.key -out ./users/client.csr  

echo "使用我们私有的 CA key 为刚才的 key 签名..."
openssl ca -in ./users/client.csr -cert ./private/ca.crt -keyfile ./private/ca.key -out ./users/client.crt -config "./conf/openssl.conf" 

echo "将证书转换为大多数浏览器都能识别的 PKCS12 文件..."
openssl pkcs12 -export -clcerts -in ./users/client.crt -inkey ./users/client.key -out ./users/client.p12  

echo "把以上生成的文件copy到nginx conf文件的ssl目录下面，如果ssl目录不存在请创建"
echo "接下请配置nginx.conf操作:"
echo " server {																						"
echo " 			...                                           "
echo "     ssl on;                                        "
echo "     ssl_certificate ca/server/server.crt;                "
echo "     ssl_certificate_key ca/server/server.key;            "
echo "     ssl_client_certificate ca/private/ca.crt;             "
echo "     ssl_verify_client on;                          "
echo "			...                                           "
echo "     }                                               "
echo "使用如下命令重新加载nginx配置"
echo "nginx -s reload"
echo "客户端使用： users/client.p12 和 private/ca.crt"

DATA_DIR=/search/odin/resin/umis/data/normal/news/
FILE_PATH=website

sh /usr/local/bin/tahr-deploy -f ${FILE_PATH} -p news -m resin
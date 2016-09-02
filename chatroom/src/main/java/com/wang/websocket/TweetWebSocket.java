package com.wang.websocket;

import com.google.gson.Gson;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 2016/8/31.
 */
public class TweetWebSocket extends TextWebSocketHandler{
    private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    public void handleTextMessage(WebSocketSession session, TextMessage message){
        if(!clients.containsKey(session.getId())){
            clients.put(session.getId(), session);
        }
        String data = message.getPayload();
        Gson g= new Gson();
        Map<String,Object> datas = g.fromJson(data, Map.class);
        String type = datas.get("type").toString();
        if("1".equals(type)){
            datas.put("pcount", clients.keySet().size() + "");
        }else if("3".equals(type)){
            clients.remove(session.getId());
            datas.put("pcount", clients.keySet().size() + "");
        }
        TextMessage tm = new TextMessage(g.toJson(datas));
        sendToAll(tm);
    }

    private void sendToAll(TextMessage tm)
    {
        try
        {
            for(WebSocketSession session : clients.values())
            {
                if(session.isOpen())
                {
                    session.sendMessage(tm);
                }
                else
                {
                    clients.remove(session.getId());
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

package spring.service;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin on 2016/10/23.
 */
public class ChatRoomService {

    private static ConcurrentHashMap<String, ResponseBodyEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitters(String key, ResponseBodyEmitter emitter) {
        emitters.put(key, emitter);
    }

    public void removeEmitter(String key) {
        emitters.remove(key);
    }

    public void sendMessage(String message) throws IOException {
        for(ResponseBodyEmitter emitter: emitters.values()) {
            emitter.send(message);
        }
    }
}

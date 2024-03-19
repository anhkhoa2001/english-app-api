package org.example.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;
import org.example.service.SocketSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SockerServiceImpl implements SocketSerivce {

    @Override
    public void sendSocketmessage(SocketIOClient senderClient, String message, String room) {
        for (SocketIOClient client: senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", message);
            }
        }
    }
}

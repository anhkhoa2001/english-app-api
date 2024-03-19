package org.example.service;

import com.corundumstudio.socketio.SocketIOClient;

public interface SocketSerivce {

    void sendSocketmessage(SocketIOClient senderClient, String message, String room);
}

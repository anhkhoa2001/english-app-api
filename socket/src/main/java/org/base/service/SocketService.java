package org.base.service;

import com.corundumstudio.socketio.SocketIOClient;

public interface SocketService<T> {

    void sendMessageToClient(SocketIOClient senderClient, T message, String room);

    T saveMessage(T message);

    Object getAllMessages(String refId, String entityRef);
}

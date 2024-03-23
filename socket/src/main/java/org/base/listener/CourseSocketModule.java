package org.base.listener;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.base.config.CommonConfig;
import org.base.config.InitialCollection;
import org.base.model.CommentModel;
import org.base.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CourseSocketModule {

    private final SocketIONamespace namespace;

    private final String COURSE_NAMESPACE = "/course";

    @Autowired
    private SocketService<CommentModel> socketService;

    @Autowired
    public CourseSocketModule(SocketIOServer server) {
        this.namespace = server.addNamespace(COURSE_NAMESPACE);

        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
        server.addEventListener("send_message", CommentModel.class, this.onChatReceived());
    }

    private DataListener<CommentModel> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            try {
                String clientId = senderClient.getSessionId().toString();
                String userId = CommonConfig.mapping.get(clientId);
                data.setSender(userId);
                data.setSendTime(new Date());

                log.info("Client {} send message {}", clientId, data);
                socketService.saveMessage(data);
                socketService.sendMessageToClient(senderClient, data, data.getRefId() + data.getEntityRef());
            } catch (Exception e) {
                e.printStackTrace();
                senderClient.disconnect();
            }
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            try {
                String refId = params.get("refId").stream().collect(Collectors.joining());
                String entityRef = params.get("entityRef").stream().collect(Collectors.joining());
                String userId = params.get("sender").stream().collect(Collectors.joining());
                String sessionId = client.getSessionId().toString();

                CommonConfig.mapping.put(sessionId, userId);
                client.joinRoom(refId + entityRef);
                log.info("Client {} join to chat room {} {}", userId, refId, entityRef);
            } catch (Exception e) {
                e.printStackTrace();
                client.disconnect();
            }
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            String refId = params.get("refId").stream().collect(Collectors.joining());
            String entityRef = params.get("entityRef").stream().collect(Collectors.joining());
            String sessionId = client.getSessionId().toString();
            client.leaveRoom(refId + entityRef);
            CommonConfig.mapping.remove(sessionId);
        };
    }
}

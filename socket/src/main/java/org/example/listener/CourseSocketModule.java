package org.example.listener;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CourseSocketModule {

    private final SocketIONamespace namespace;

    private final String COURSE_NAMESPACE = "/course";


    @Autowired
    public CourseSocketModule(SocketIOServer server) {
        this.namespace = server.addNamespace(COURSE_NAMESPACE);

        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
        server.addEventListener("send_message", String.class, this.onChatReceived());
    }

    private DataListener<String> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info("receive message on socker {}", data.toString());
            //socketService.saveMessage(senderClient, data);
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            try {
                String room = params.get("room").stream().collect(Collectors.joining());
                String username = params.get("username").stream().collect(Collectors.joining());
                client.joinRoom(room);
                client.sendEvent("read_message", "khoa dep trai");
                System.out.println(room + "|" + username + "|" + client.getSessionId());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            /*String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());
            client.joinRoom(room);*/
            //socketService.saveInfoMessage(client, String.format(Constants.WELCOME_MESSAGE, username), room);
            //log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);

        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
//            String room = params.get("room").stream().collect(Collectors.joining());
//            String username = params.get("username").stream().collect(Collectors.joining());
            //socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, username), room);
            //log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }
}

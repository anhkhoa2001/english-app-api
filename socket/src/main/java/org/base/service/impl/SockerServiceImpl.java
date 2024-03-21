package org.base.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;
import org.base.dto.CommentTree;
import org.base.model.CommentModel;
import org.base.repository.CommentRepository;
import org.base.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SockerServiceImpl implements SocketService<CommentModel> {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void sendMessageToClient(SocketIOClient senderClient, CommentModel message, String room) {
        for (SocketIOClient client: senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", message);
            }
        }
    }

    @Override
    public CommentModel saveMessage(CommentModel message) {
        return commentRepository.save(message);
    }

    @Override
    public Object getAllMessages(String refId, String tableRef) {
        List<CommentModel> comments = commentRepository.getAllByRefIdAndEntityRef(refId, tableRef);
        return buildTree(comments);
    }

    private Object buildTree(List<CommentModel> comments) {
        Map<Integer, CommentTree> map = new HashMap<>();
        List<CommentTree> tree = new ArrayList<>();

        for(CommentModel cmt : comments) {
            //node cha
            if(cmt.getParentId() == 0) {
                CommentTree parent = new CommentTree(cmt);
                map.put(cmt.getCommentId(), parent);
                tree.add(parent);
            } else {
                CommentTree parent = map.get(cmt.getParentId());
                List<CommentTree> childs = parent.getChildrens();
                CommentTree child = new CommentTree(cmt);
                childs.add(child);
                parent.setHasChild(true);
                map.put(cmt.getCommentId(), child);
            }
        }

        return tree;
    }
}

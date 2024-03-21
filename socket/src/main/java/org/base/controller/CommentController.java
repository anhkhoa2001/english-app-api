package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.model.CommentModel;
import org.base.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@Slf4j
@EnableWrapResponse
public class CommentController {

    @Autowired
    private SocketService<CommentModel> socketService;

    @GetMapping("/get-all-comments")
    public ResponseEntity getAllComment(@RequestParam String refId,
                                        @RequestParam String entityRef) {
        return ResponseEntity.ok(socketService.getAllMessages(refId, entityRef));
    }
}

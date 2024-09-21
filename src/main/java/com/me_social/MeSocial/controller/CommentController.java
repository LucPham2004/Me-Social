package com.me_social.MeSocial.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.me_social.MeSocial.entity.modal.Comment;

@Controller
public class CommentController {

    @MessageMapping("/comments")
    @SendTo("/topic/comments")
    public Comment send(Comment comment) {
        // Xử lý bình luận mới, ví dụ: lưu vào database
        return comment;
    }
}
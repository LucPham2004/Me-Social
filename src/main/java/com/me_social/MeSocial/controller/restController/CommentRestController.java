package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.CommentRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.CommentResponse;
import com.me_social.MeSocial.service.CommentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class CommentRestController {

    CommentService commentService;

    // Get by id
    @GetMapping("/{id}")
    public ApiResponse<CommentResponse> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    // Get comments by post
    @GetMapping("/post/{postId}/{pageNum}")
    public ApiResponse<Page<CommentResponse>> getCommentsByPost(
        @RequestParam Long postId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return commentService.getCommentsByPost(postId, pageNum);
    }

    // Get comments by user
    @GetMapping("/user/{userId}/{pageNum}")
    public ApiResponse<Page<CommentResponse>> getCommentsByUser(
        @RequestParam Long userId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return commentService.getCommentsByUser(userId, pageNum);
    }

    // POST
    // Not in real-time update yet
    @PostMapping("/new")
    public ApiResponse<CommentResponse> createcomment(@RequestBody CommentRequest request) {
        return commentService.createcomment(request);
    }

    // DELETE
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }

    // PUT
    @PutMapping("/edit") 
    public ApiResponse<CommentResponse> editcomment(@RequestBody CommentRequest request) {
        return commentService.editcomment(request);
    }
}
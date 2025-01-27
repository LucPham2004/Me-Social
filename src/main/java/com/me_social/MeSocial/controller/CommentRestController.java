    package com.me_social.MeSocial.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.me_social.MeSocial.mapper.CommentMapper;
import com.me_social.MeSocial.service.CommentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentRestController {

    CommentService commentService;
    CommentMapper mapper;

    // Get by id
    @GetMapping("/{userId}/{id}")
    public ApiResponse<CommentResponse> getCommentById(@PathVariable Long userId, @PathVariable Long id) {
        var comment = commentService.getCommentById(id);
        return ApiResponse.<CommentResponse>builder()
                .code(1000)
                .message("Get comment with ID " + id + " successfully!")
                .result(mapper.toCommentResponse(userId, comment))
                .build();
    }

    // Get comments by post
    // only get top comment (parentCommentId == null)
    @GetMapping("/post/top-comment")
    public ApiResponse<Page<CommentResponse>> getTopCommentsByPost(@RequestParam Long userId, @RequestParam Long postId, Pageable pageable) {
        var comments = commentService.getTopLevelCommentsByPost(postId, pageable);
        return ApiResponse.<Page<CommentResponse>>builder()
                .code(1000)
                .message("Get comments of the post with ID " + postId + " successfully!")
                .result(comments.map(comment -> mapper.toCommentResponse(userId, comment)))
                .build();
    }



    // Get comments by user
    @GetMapping("/user")
    public ApiResponse<Page<CommentResponse>> getCommentsByUser(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int pageNum) {
        var comments = commentService.getCommentsByUser(userId, pageNum);
        return ApiResponse.<Page<CommentResponse>>builder()
                .code(1000)
                .message("Get comments of the user with ID " + userId + " successfully!")
                .result(comments.map(comment -> mapper.toCommentResponse(userId, comment)))
                .build();
    }

    @GetMapping("/child-comments")
    public ApiResponse<Page<CommentResponse>> getAllChildComments(@RequestParam Long userId, @RequestParam Long parentCommentId, Pageable pageable) {
        var comments = commentService.getChildCommentByTopLevelComment(parentCommentId, pageable);

        return ApiResponse.<Page<CommentResponse>>builder()
                .code(1000)
                .message("Get all children comments for parent comment with ID " + parentCommentId)
                .result(comments.map(comment -> mapper.toCommentResponse(userId, comment)))
                .build();
    }

    // Get by id
    @GetMapping("/post/count/{postId}")
    public ApiResponse<Integer> getPostCommentCount(@PathVariable Long postId) {
        var comment = commentService.getPostCommentCount(postId);
        return ApiResponse.<Integer>builder()
                .code(1000)
                .message("Get comment count for post with ID " + postId + " successfully!")
                .result(comment)
                .build();
    }

    // POST
    // Not in real-time update yet
    @PostMapping("/new")
    public ApiResponse<CommentResponse> createComment(@RequestBody CommentRequest request) {
        var comment = commentService.createcomment(request);
        return ApiResponse.<CommentResponse>builder()
                .code(1000)
                .message("Create a new comment successfully!")
                .result(mapper.toCommentResponse(request.getUserId(), comment))
                .build();
    }

    // DELETE
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete comment with ID " + commentId + " successfully!")
                .build();
    }

    // PUT
    @PutMapping("/edit")
    public ApiResponse<CommentResponse> editComment(@RequestBody CommentRequest request) {
        var comment = commentService.editcomment(request);
        return ApiResponse.<CommentResponse>builder()
                .code(1000)
                .message("Update comment with ID" + request.getId() + " successfully!")
                .result(mapper.toCommentResponse(request.getUserId(), comment))
                .build();
    }
}
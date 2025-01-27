package com.me_social.MeSocial.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Like;
import com.me_social.MeSocial.service.LikeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class LikeController {
    LikeService likeService;

    // get post like count
    @GetMapping("/post/count/{postId}")
    public ApiResponse<Integer> getPostLikeCount(@PathVariable Long postId) {
        int likeCount = likeService.getPostLikeCount(postId);
        return ApiResponse.<Integer>builder()
            .code(1000)
            .message("Create post like successfully")
            .result(likeCount)
            .build();
    }

    // get comment like count
    @GetMapping("/comment/count/{commentId}")
    public ApiResponse<Integer> getCommentLikeCount(@PathVariable Long commentId) {
        int likeCount = likeService.getCommentLikeCount(commentId);
        return ApiResponse.<Integer>builder()
            .code(1000)
            .message("Create post like successfully")
            .result(likeCount)
            .build();
    }
    
    // Create Post Like
    @PostMapping("/post/add/{userId}/{postId}")
    public ApiResponse<Like> createPostLike(@PathVariable Long userId, @PathVariable Long postId) {
        Like like = likeService.createPostLike(userId, postId);
        return ApiResponse.<Like>builder()
            .code(1000)
            .message("Create post like successfully")
            .result(like)
            .build();
    }

    // Create Comment Like
    @PostMapping("/comment/add/{userId}/{commentId}")
    public ApiResponse<Like> createCommentLike(@PathVariable Long userId, @PathVariable Long commentId) {
        Like like = likeService.createCommentLike(userId, commentId);
        return ApiResponse.<Like>builder()
            .code(1000)
            .message("Create comment like successfully")
            .result(like)
            .build();
    }

    // Delete Post Like
    @DeleteMapping("/post/remove/{userId}/{postId}")
    public ApiResponse<String> deletePostLike(@PathVariable Long userId, @PathVariable Long postId) {
        likeService.deletePostLike(userId, postId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Delete post like successfully")
            .result("")
            .build();
    }

    // Delete Comment Like
    @DeleteMapping("/comment/remove/{userId}/{commentId}")
    public ApiResponse<String> deleteCommentLike(@PathVariable Long userId, @PathVariable Long commentId) {
        likeService.deleteCommentLike(userId, commentId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Delete comment like successfully")
            .result("")
            .build();
    }

}

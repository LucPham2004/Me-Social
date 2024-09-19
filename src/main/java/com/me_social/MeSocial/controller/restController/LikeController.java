package com.me_social.MeSocial.controller.restController;

import org.springframework.web.bind.annotation.DeleteMapping;
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

    // Create Post Like
    @PostMapping("/post/add/{userId}/{postId}")
    public ApiResponse<Like> createLPostLike(@PathVariable Long userId, @PathVariable Long postId) {
        return likeService.createPostLike(userId, postId);
    }

    // Create Comment Like
    @PostMapping("/comment/add/{userId}/{commentId}")
    public ApiResponse<Like> createCommentLike(@PathVariable Long userId, @PathVariable Long commentId) {
        return likeService.createCommentLike(userId, commentId);
    }

    // Delete Post Like
    @DeleteMapping("/post/remove/{userId}/{postId}")
    public ApiResponse<String> deletePostLike(@PathVariable Long userId, @PathVariable Long postId) {
        return likeService.deletePostLike(userId, postId);
    }

    // Delete Comment Like
    @DeleteMapping("/post/remove/{userId}/{commentId}")
    public ApiResponse<String> deleteCommentLike(@PathVariable Long userId, @PathVariable Long commentId) {
        return likeService.deleteCommentLike(userId, commentId);
    }

}

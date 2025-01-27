package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.modal.Like;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.CommentRepository;
import com.me_social.MeSocial.repository.LikeRepository;
import com.me_social.MeSocial.repository.PostRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class LikeService {

    LikeRepository likeRepository;
    UserRepository userRepository;
    UserService userService;
    PostRepository postRepository;
    CommentRepository commentRepository;

    // Get post like count
    public int getPostLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    // Get comment like count
    public int getCommentLikeCount(Long commentId) {
        return likeRepository.countByCommentId(commentId);
    }

    // Create Post Like
    public Like createPostLike(Long userId, Long postId) {
        if (!userRepository.existsById(userId) || !postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndPostId(userId, postId);
        if (like == null) {
            like = new Like();
            like.setUser(userService.findById(userId));
            like.setPost(postRepository.findById(postId).get());

            return likeRepository.save(like);
        } else {
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
    }

    // Create Comment Like
    public Like createCommentLike(Long userId, Long commentId) {
        if (!userRepository.existsById(userId) || !commentRepository.existsById(commentId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId);
        if (like == null) {
            like = new Like();
            like.setUser(userService.findById(userId));
            like.setComment(commentRepository.findById(commentId).get());
            like.setCreatedAt(LocalDateTime.now());
            return likeRepository.save(like);
        } else {
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }
    }

    // Delete Post Like
    public void deletePostLike(Long userId, Long postId) {
        if (!postRepository.existsById(postId) || !userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndPostId(userId, postId);
        if (like != null) {
            likeRepository.delete(like);
        } else {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
    }

    // Delete Comment Like
    public void deleteCommentLike(Long userId, Long commentId) {
        if (!commentRepository.existsById(commentId) || !userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId);
        if (like != null) {
            likeRepository.delete(like);
        } else {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
    }
}

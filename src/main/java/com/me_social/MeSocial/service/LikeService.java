package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
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
    PostRepository postRepository;
    CommentRepository commentRepository;

    // Create Post Like
    public ApiResponse<Like> createPostLike(Long userId, Long postId) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndPostId(userId, postId);
        if(like == null) {
            like = new Like();
            like.setUser(userRepository.findById(userId));

            if(postRepository.existsById(postId)) {
                like.setPost(postRepository.findById(postId));
            }

            like.setCreatedAt(LocalDateTime.now());

            ApiResponse<Like> apiResponse = new ApiResponse<>();

            apiResponse.setCode(1000);
            apiResponse.setMessage("Create like successfully");
            apiResponse.setResult(likeRepository.save(like));

            return apiResponse;
        }
        else throw new AppException(ErrorCode.ENTITY_EXISTED);
        
    }

    // Create Comment Like
    public ApiResponse<Like> createCommentLike(Long userId, Long commentId) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId);
        if(like == null) {
            like = new Like();
            like.setUser(userRepository.findById(userId));

            if(commentRepository.existsById(commentId)) {
                like.setComment(commentRepository.findById(commentId));
            }

            like.setCreatedAt(LocalDateTime.now());

            ApiResponse<Like> apiResponse = new ApiResponse<>();

            apiResponse.setCode(1000);
            apiResponse.setMessage("Create like successfully");
            apiResponse.setResult(likeRepository.save(like));

            return apiResponse;
        }
        else throw new AppException(ErrorCode.ENTITY_EXISTED);
    }

    // Delete Post Like
    public ApiResponse<String> deletePostLike(Long userId, Long postId) {
        if(!postRepository.existsById(postId) || !userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndPostId(userId, postId);
        if(like != null)
            likeRepository.delete(like);
        else throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Delete like successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    // Delete Comment Like
    public ApiResponse<String> deleteCommentLike(Long userId, Long commentId) {
        if(!commentRepository.existsById(commentId) || !userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId);
        if(like != null)
            likeRepository.delete(like);
        else throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Delete like successfully");
        apiResponse.setResult("");

        return apiResponse;
    }
}

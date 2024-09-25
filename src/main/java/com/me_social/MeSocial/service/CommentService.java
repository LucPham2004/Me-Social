package com.me_social.MeSocial.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.request.CommentRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.CommentResponse;
import com.me_social.MeSocial.entity.modal.Comment;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.CommentMapper;
import com.me_social.MeSocial.repository.CommentRepository;
import com.me_social.MeSocial.repository.PostRepository;
import com.me_social.MeSocial.repository.UserRepository;
import com.me_social.MeSocial.utils.PaginationUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    PostRepository postRepository;
    CommentMapper mapper;

    static int COMMENTS_PER_PAGE = 20;

    // Get by id
    public ApiResponse<CommentResponse> getCommentById(Long id) {
        if(!commentRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get comment by id successfully");
        apiResponse.setResult(mapper.toCommentResponse(commentRepository.findById(id)));

        return apiResponse;
    }

    // Get comments by post
    public ApiResponse<Page<CommentResponse>> getCommentsByPost(Long postId, int pageNum) {
        if(!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, COMMENTS_PER_PAGE);

        ApiResponse<Page<CommentResponse>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get comments successfully");
        apiResponse.setResult(PaginationUtil.convertSetToPage(commentRepository.findByPostId(postId)
            .stream()
            .map(mapper::toCommentResponse)
            .collect(Collectors.toSet()), pageable));

        return apiResponse;
    }

    // Get comments by user
    public ApiResponse<Page<CommentResponse>> getCommentsByUser(Long userId, int pageNum) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, COMMENTS_PER_PAGE);

        ApiResponse<Page<CommentResponse>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get comments successfully");
        apiResponse.setResult(PaginationUtil.convertSetToPage(commentRepository.findByUserId(userId)
            .stream()
            .map(mapper::toCommentResponse)
            .collect(Collectors.toSet()), pageable));

        return apiResponse;
    }

    // POST
    // Not in real-time update yet
    public ApiResponse<CommentResponse> createcomment(CommentRequest request) {
        if(!userRepository.existsById(request.getUserId()) || !postRepository.existsById(request.getPostId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Comment comment = mapper.toComment(request);
        comment.setCreatedAt(LocalDateTime.now());

        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Comment created successfully");
        apiResponse.setResult(mapper.toCommentResponse(commentRepository.save(comment)));

        return apiResponse;
    }

    // DELETE
    public ApiResponse<String> deleteComment(Long commentId) {
        if(!commentRepository.existsById(commentId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        commentRepository.delete(commentRepository.findById(commentId));

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Delete comment successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

     // PUT
    @Transactional
    public ApiResponse<CommentResponse> editcomment(CommentRequest request) {
        if(!commentRepository.existsById(request.getId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        
        Comment comment = commentRepository.findById(request.getId());
        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        ApiResponse<CommentResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Edit comment successfully");
        apiResponse.setResult(mapper.toCommentResponse(commentRepository.save(comment)));

        return apiResponse;
    }
}

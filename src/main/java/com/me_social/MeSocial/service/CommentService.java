package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.tokens.CommentToken;

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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    PostRepository postRepository;
    CommentMapper mapper;

    static int COMMENTS_PER_PAGE = 20;

    // Get by id
    public Comment getCommentById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return commentRepository.findById(id);
    }

    // Get comments by post
    public Page<Comment> getCommentsByPost(Long postId, int pageNum) {
        if (!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, COMMENTS_PER_PAGE);

        return commentRepository.findByPostId(postId, pageable);
    }

    // Get comments by user
    public Page<Comment> getCommentsByUser(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, COMMENTS_PER_PAGE);

        return commentRepository.findByUserId(userId, pageable);
    }

    // POST
    // Not in real-time update yet
    public Comment createcomment(CommentRequest request) {
        if (!userRepository.existsById(request.getUserId()) || !postRepository.existsById(request.getPostId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Comment comment = mapper.toComment(request);

        return commentRepository.save(comment);
    }

    // DELETE
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        commentRepository.delete(commentRepository.findById(commentId));
    }

    // PUT
    @Transactional
    public Comment editcomment(CommentRequest request) {
        if (!commentRepository.existsById(request.getId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Comment comment = commentRepository.findById(request.getId());
        comment.setContent(request.getContent());

        return commentRepository.save(comment);
    }
}

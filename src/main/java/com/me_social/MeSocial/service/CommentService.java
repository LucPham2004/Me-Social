package com.me_social.MeSocial.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.request.CommentRequest;
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
        return commentRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    public Page<Comment> getTopLevelCommentsByPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return commentRepository.findTopLevelCommentsByPostId(postId, pageable);
    }

    public Page<Comment> getChildCommentByTopLevelComment(Long parentCommentId, Pageable pageable) {
        if (!commentRepository.existsById(parentCommentId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        return commentRepository.findAllChildComments(parentCommentId, pageable);
    }

    // Get comments by user
    public Page<Comment> getCommentsByUser(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, COMMENTS_PER_PAGE);

        return commentRepository.findByUserId(userId, pageable);
    }

    // Get post comment count
    public int getPostCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    // POST
    // Not in real-time update yet
    public Comment createcomment(CommentRequest request) {
        if (!userRepository.existsById(request.getUserId()) || !postRepository.existsById(request.getPostId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Comment comment = mapper.toComment(request);

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));

            comment.setParentComment(parentComment);
        }

        return commentRepository.save(comment);
    }

    // DELETE
    public void deleteComment(Long commentId) {
        commentRepository.delete(commentRepository.findById(commentId)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED)));
    }

    // PUT
    @Transactional
    public Comment editcomment(CommentRequest request) {
        Comment comment = commentRepository.findById(request.getId())
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        comment.setContent(request.getContent());

        return commentRepository.save(comment);
    }

    public static Long countAllResponses(Comment comment) {
        if (comment.getChildComments() == null || comment.getChildComments().isEmpty()) {
            return 0L;
        }
        Long count = (long) comment.getChildComments().size();
        for (Comment child : comment.getChildComments()) {
            count += countAllResponses(child);
        }
        return count;
    }
}

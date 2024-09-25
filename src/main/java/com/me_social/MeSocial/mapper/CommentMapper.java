package com.me_social.MeSocial.mapper;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.CommentRequest;
import com.me_social.MeSocial.entity.dto.response.CommentResponse;
import com.me_social.MeSocial.entity.modal.Comment;
import com.me_social.MeSocial.repository.PostRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class CommentMapper {
    public CommentMapper(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    UserRepository userRepository;
    PostRepository postRepository;

    public Comment toComment(CommentRequest request) {
        Comment comment = new Comment();

        comment.setContent(request.getContent());
        comment.setId(request.getId());
        comment.setUser(userRepository.findById(request.getUserId()));
        comment.setPost(postRepository.findById(request.getPostId()));
        
        return comment;
    }
    
    public CommentResponse toCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();

        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setId(comment.getId());
        response.setUpdatedAt(comment.getUpdatedAt());
        if(comment.getLikes() != null)
            response.setLikeNum(comment.getLikes().size());
        else
            response.setLikeNum(0);
        
        return response;
    }
}

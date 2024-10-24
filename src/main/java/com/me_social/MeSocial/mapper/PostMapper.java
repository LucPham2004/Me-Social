package com.me_social.MeSocial.mapper;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.PostRequest;
import com.me_social.MeSocial.entity.dto.response.PostResponse;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.repository.CommentRepository;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.LikeRepository;
import com.me_social.MeSocial.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class PostMapper {

    UserService userService;
    GroupRepository groupRepository;
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    
    public Post toPost(PostRequest request) {{
        Post post = new Post();
        post.setContent(request.getContent());
        post.setPrivacy(request.getPrivacy());

        post.setUser(userService.findById(request.getUserId()).get());
        
        if(request.getGroupId() != null)
            post.setGroup(groupRepository.findById(request.getGroupId()).get());

        return post;
    }}

    public PostResponse toPostResponse(Post post) {
        PostResponse response = new PostResponse();

        response.setId(post.getId());
        response.setUserId(post.getUser().getId());
        response.setUserFullName(post.getUser().getLastName() + " " + post.getUser().getFirstName());
        if(post.getGroup() != null) {
            response.setGroupId(post.getGroup().getId());
            response.setGroupName(post.getGroup().getName());
        }
        response.setContent(post.getContent());
        response.setPrivacy(post.getPrivacy());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setLikeNum(likeRepository.countByPostId(post.getId()));
        response.setCommentNum(commentRepository.countByPostId(post.getId()));
        
        return response;
    }
}

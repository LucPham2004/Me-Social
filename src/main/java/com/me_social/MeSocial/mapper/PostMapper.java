package com.me_social.MeSocial.mapper;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.PostRequest;
import com.me_social.MeSocial.entity.dto.response.PostResponse;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class PostMapper {

    public PostMapper(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    UserRepository userRepository;
    GroupRepository groupRepository;
    
    public Post toPost(PostRequest request) {{
        Post post = new Post();
        post.setContent(request.getContent());
        post.setPrivacy(request.getPrivacy());

        post.setUser(userRepository.findById(request.getUserId()));
        
        if(request.getGroupId() != null)
            post.setGroup(groupRepository.findById(request.getGroupId()));

        return post;
    }}

    public PostResponse toPostResponse(Post post) {
        PostResponse response = new PostResponse();

        response.setId(post.getId());
        response.setUserId(post.getUser().getId());
        if(post.getGroup() != null)
            response.setGroupId(post.getGroup().getId());
        response.setContent(post.getContent());
        response.setPrivacy(post.getPrivacy());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        if(post.getLikes() != null)
            response.setLikeNum(post.getLikes().size());
        else
            response.setLikeNum(0);
        
        if(post.getComments() != null)
            response.setCommentNum(post.getComments().size());
        else
            response.setCommentNum(0);
        
        return response;
    }
}

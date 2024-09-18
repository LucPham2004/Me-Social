package com.me_social.MeSocial.mapper;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.PostRequest;
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
}

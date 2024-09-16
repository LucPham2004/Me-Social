package com.me_social.MeSocial.mapper;

import org.mapstruct.Mapper;

import com.me_social.MeSocial.entity.dto.request.PostCreationRequest;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Mapper(componentModel = "spring")
@FieldDefaults(level=AccessLevel.PRIVATE)
public class PostMapper {

    UserRepository userRepository;
    GroupRepository groupRepository;
    
    public Post toPost(PostCreationRequest request) {{
        Post post = new Post();
        post.setContent(request.getContent());
        post.setPrivacy(request.getPrivacy());

        post.setUser(userRepository.findById(request.getUserId()));
        post.setGroup(groupRepository.findById(request.getGroupId()));

        return post;
    }}
}

package com.me_social.MeSocial.mapper;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.PostRequest;
import com.me_social.MeSocial.entity.dto.response.PostResponse;
import com.me_social.MeSocial.entity.modal.Media;
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
        boolean isLiked = likeRepository.existsByPostIdAndUserId(post.getId(), post.getUser().getId());
        response.setLiked(isLiked);

        response.setId(post.getId());
        response.setUserId(post.getUser().getId());
        response.setUserFullName(post.getUser().getUsername());
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

        Set<Media> medias = post.getMedias();
        if (medias != null && !medias.isEmpty()) {
            String[] publicIds = medias.stream()
                .map(Media::getPublicId)
                .toArray(String[]::new);

            String[] urls = medias.stream()
                .map(Media::getUrl)
                .toArray(String[]::new);

            response.setPublicIds(publicIds);
            response.setUrls(urls);
        }
        
        return response;
    }
}

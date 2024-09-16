package com.me_social.MeSocial.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.request.PostCreationRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.entity.modal.Tag;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.PostMapper;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.PostRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class PostService {
    PostRepository postRepository;
    UserRepository userRepository;
    GroupRepository groupRepository;
    PostMapper postMapper;
    TagService tagService;

    static int POSTS_PER_PAGE = 5;

    // GET
    // Get Posts By User
    public ApiResponse<Page<Post>> getPostsByUser(Long userId, int pageNum) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, POSTS_PER_PAGE);

        ApiResponse<Page<Post>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Posts successfully");
        apiResponse.setResult(postRepository.findAllByUserId(userId, pageable));

        return apiResponse;
    }
    
    // Get Posts By Group
    public ApiResponse<Page<Post>> getPostsByGroup(Long groupId, int pageNum) {
        if(!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, POSTS_PER_PAGE);

        ApiResponse<Page<Post>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Posts successfully");
        apiResponse.setResult(postRepository.findAllByGroupId(groupId, pageable));

        return apiResponse;
    }

    // POST
    // Create New Post
    public ApiResponse<Post> createPost(PostCreationRequest request) {
        Post post = postMapper.toPost(request);

        Set<Tag> tags = new HashSet<>();

        for(String nameTag: request.getNameTag()) {
            tags.add(tagService.createTag(nameTag, post));
        }

        post.setTags(tags);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);

        ApiResponse<Post> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Post created successfully");
        apiResponse.setResult(post);

        return apiResponse;
    }

    // Delete Post
    public ApiResponse<String> deletePost(Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        postRepository.delete(postRepository.findById(postId));

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Post deleted successfully");
        
        return apiResponse;
    }

    // Edit Post
    public ApiResponse<Post> editPost(PostCreationRequest request) {
        if(!postRepository.existsById(request.getId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Post post = postRepository.findById(request.getId());
        post.setContent(request.getContent());
        post.setPrivacy(request.getPrivacy());

        for(Tag tag: post.getTags()) {
            tagService.deleteTag(tag);
        }

        Set<Tag> tags = new HashSet<>();

        for(String nameTag: request.getNameTag()) {
            tags.add(tagService.createTag(nameTag, post));
        }
        post.setTags(tags);
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);

        ApiResponse<Post> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Post edited successfully");
        apiResponse.setResult(post);

        return apiResponse;
    }
}
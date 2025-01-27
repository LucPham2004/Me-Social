package com.me_social.MeSocial.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.PostRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.PostResponse;
import com.me_social.MeSocial.mapper.PostMapper;
import com.me_social.MeSocial.service.PostService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class PostController {
    PostService postService;
    PostMapper postMapper;

    // GET

    // Get Posts for NewsFeed
    @GetMapping("/newsfeed")
    public ApiResponse<Page<PostResponse>> getPostsForNewsFeed(
            @RequestParam Long userId,
            Pageable pageable) {
        var posts = this.postService.getPostsForNewsFeed(userId, pageable);
        return ApiResponse.<Page<PostResponse>>builder()
                            .code(1000)
                            .message("Get Posts for Newsfeed for User with ID = " + userId + " successfully!")
                            .result(posts.map(post -> postMapper.toPostResponse(post, userId)))
                            .build();
    }

    // Get Posts for Group Activities 
    @GetMapping("/newsfeed/groups")
    public ApiResponse<Page<PostResponse>> getPostsForUserJoinedGroupNewsFeed(
            @RequestParam Long userId, 
            @RequestParam(defaultValue = "0") int pageNum) {
        var posts = this.postService.getPostsForUserJoinedGroupNewsFeed(userId, pageNum);
        return ApiResponse.<Page<PostResponse>>builder()
                            .code(1000)
                            .message("Get Posts for Group Activities for User with ID = " + userId + " successfully!")
                            .result(posts.map(post -> postMapper.toPostResponse(post, userId)))
                            .build();
    }

    // Get Posts By User
    @GetMapping("/user")
    public ApiResponse<Page<PostResponse>> getPostsByUser(
            @RequestParam Long userId, 
            @RequestParam(defaultValue = "0") int pageNum) {
        var posts = this.postService.getPostsByUser(userId, pageNum);
        return ApiResponse.<Page<PostResponse>>builder()
                            .code(1000)
                            .message("Get Posts by User with ID = " + userId + " successfully!")
                            .result(posts.map(post -> postMapper.toPostResponse(post, userId)))
                            .build();
    }
    
    // Get Posts By Group
    @GetMapping("/group")
    public ApiResponse<Page<PostResponse>> getPostsByGroup(
            @RequestParam Long userId,
            @RequestParam Long groupId, 
            @RequestParam(defaultValue = "0") int pageNum) {
        var posts = this.postService.getPostsByGroup(groupId, pageNum);
        return ApiResponse.<Page<PostResponse>>builder()
                            .code(1000)
                            .message("Get Posts by Group with ID = " + groupId + " successfully!")
                            .result(posts.map(post -> postMapper.toPostResponse(post, userId)))
                            .build();
    }

    // Get Posts By Tag
    @GetMapping("/tag")
    public ApiResponse<Page<PostResponse>> getPostsByTag(
            @RequestParam Long userId,
            @RequestParam Long tagId, 
            @RequestParam(defaultValue = "0") int pageNum) {
        var posts = this.postService.getPostsByTag(tagId, pageNum);
        return ApiResponse.<Page<PostResponse>>builder()
                            .code(1000)
                            .message("Get Posts by Tag with ID = " + tagId + " successfully!")
                            .result(posts.map(post -> postMapper.toPostResponse(post, userId)))
                            .build();
    }

    // POST
    // Create New Post
    @PostMapping("/new")
    public ApiResponse<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        var post = this.postService.createPost(request);
        return ApiResponse.<PostResponse>builder()
                            .code(1000)
                            .message("Create Post successfully!")
                            .result(postMapper.toPostResponse(post, request.getUserId()))
                            .build();
    }

    // Delete Post
    @DeleteMapping("/delete/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        this.postService.deletePost(postId);
        return ApiResponse.<Void>builder()
                            .code(1000)
                            .message("Delete Post with ID = " + postId + " successfully!")
                            .build();
    }

    // Edit Post
    @PutMapping("/edit")
    public ApiResponse<PostResponse> editPost(@Valid @RequestBody PostRequest request) {
        var post = this.postService.editPost(request);
        return ApiResponse.<PostResponse>builder()
                            .code(1000)
                            .message("Edit Post with ID = " + request.getId() + " successfully!")
                            .result(postMapper.toPostResponse(post, request.getUserId()))
                            .build();
    }

    @GetMapping("favorite")
    public ApiResponse<Page<PostResponse>> getFavoritePostByUser(@RequestParam Long userId, Pageable pageable) {
        var posts = this.postService.getFavoritePostByUser(userId, pageable);

        return ApiResponse.<Page<PostResponse>>builder()
                .code(1000)
                .message("Get all favorite post for user with id " + userId + " successfully!")
                .result(posts.map(post -> postMapper.toPostResponse(post, userId)))
                .build();
    }
}

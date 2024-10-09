package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
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

    // GET

    // Get Posts for NewsFeed
    @GetMapping("/newsfeed")
    public ApiResponse<Page<PostResponse>> getPostsForNewsFeed(
        @RequestParam Long userId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return postService.getPostsForNewsFeed(userId, pageNum);
    }

    // Get Posts for Group Activities 
    @GetMapping("/newsfeed/groups")
    public ApiResponse<Page<PostResponse>> getPostsForUserJoinedGroupNewsFeed(
        @RequestParam Long userId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return postService.getPostsForUserJoinedGroupNewsFeed(userId, pageNum);
    }

    // Get Posts By User
    @GetMapping("/user")
    public ApiResponse<Page<PostResponse>> getPostsByUser(
        @RequestParam Long userId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return postService.getPostsByUser(userId, pageNum);
    }
    
    // Get Posts By Group
    @GetMapping("/group")
    public ApiResponse<Page<PostResponse>> getPostsByGroup(
        @RequestParam Long groupId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return postService.getPostsByGroup(groupId, pageNum);
    }

    // Get Posts By Tag
    @GetMapping("/tag")
    public ApiResponse<Page<PostResponse>> getPostsByTag(
        @RequestParam Long tagId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return postService.getPostsByTag(tagId, pageNum);
    }

    // POST
    // Create New Post
    @PostMapping("/new")
    public ApiResponse<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        return postService.createPost(request);
    }

    // Delete Post
    @DeleteMapping("/delete/{postId}")
    public ApiResponse<String> deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }

    // Edit Post
    @PutMapping("/edit")
    public ApiResponse<PostResponse> editPost(@Valid @RequestBody PostRequest request) {
        
        return postService.editPost(request);
    }
}

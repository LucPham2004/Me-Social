package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.PostRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.service.PostService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class PostController {
    PostService postService;

    // GET
    // Get Posts By User
    @GetMapping("/get/byUser/{userId}/{pageNum}")
    public ApiResponse<Page<Post>> getPostsByUser(@PathVariable Long userId, @PathVariable int pageNum) {
        return postService.getPostsByUser(userId, pageNum);
    }
    
    // Get Posts By Group
    @GetMapping("/get/byGroup/{groupId}/{pageNum}")
    public ApiResponse<Page<Post>> getPostsByGroup(@PathVariable Long groupId, @PathVariable int pageNum) {
        return postService.getPostsByGroup(groupId, pageNum);
    }

    // POST
    // Create New Post
    @PostMapping("/new")
    public ApiResponse<Post> createPost(@RequestBody PostRequest request) {
        return postService.createPost(request);
    }

    // Delete Post
    @DeleteMapping("/delete/{postId}")
    public ApiResponse<String> deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }

    // Edit Post
    @PutMapping("/edit")
    public ApiResponse<Post> editPost(@RequestBody PostRequest request) {
        
        return postService.editPost(request);
    }
}

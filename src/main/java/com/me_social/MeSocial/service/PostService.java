package com.me_social.MeSocial.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.request.PostRequest;
import com.me_social.MeSocial.entity.modal.Media;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.entity.modal.Tag;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.PostMapper;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.MediaRepository;
import com.me_social.MeSocial.repository.PostRepository;
import com.me_social.MeSocial.repository.TagRepository;
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
    UserService userService;
    GroupRepository groupRepository;
    TagRepository tagRepository;
    PostMapper postMapper;
    MediaRepository mediaRepository;
    TagService tagService;

    static int POSTS_PER_PAGE = 10;

    // GET

    // Get Posts for NewsFeed
    public Page<Post> getPostsForNewsFeed(Long userId, int pageNum) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, POSTS_PER_PAGE);

        return postRepository.findNewsfeedPosts(userId, pageable);
    }

    // Get Posts for Group Activities 
    public Page<Post> getPostsForUserJoinedGroupNewsFeed(Long userId, int pageNum) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, POSTS_PER_PAGE);

        return postRepository.findJoinedGroupPosts(userId, pageable);
    }

    // Get Posts By User
    public Page<Post> getPostsByUser(Long userId, int pageNum) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, POSTS_PER_PAGE);
        
        return postRepository.findPostsByUserExceptGroup(userId, pageable);
    }
    
    // Get Posts By Group
    public Page<Post> getPostsByGroup(Long groupId, int pageNum) {
        if(!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        
        Pageable pageable = PageRequest.of(pageNum, POSTS_PER_PAGE);
    
        return postRepository.findByGroupIdOrderByCreatedAtDesc(groupId, pageable);
    }
    

    // Get Posts By Tag
    public Page<Post> getPostsByTag(Long tagId, int pageNum) {
        if(!tagRepository.existsById(tagId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, POSTS_PER_PAGE);

        return postRepository.findByTagsId(tagId, pageable);
    }

    // POST
    // Create New Post
    public Post createPost(PostRequest request) {
        Post post = postMapper.toPost(request);

        if(request.getNameTag() != null) {
            
            Set<Tag> tags = new HashSet<>();

            for(String nameTag: request.getNameTag()) {
                Tag tag = tagRepository.findByName(nameTag);
                if(tag != null) 
                    tags.add(tag);
                else
                    tags.add(tagService.createTag(nameTag));
            }
    
            post.setTags(tags);
        }

        post.setUser(userService.findById(request.getUserId())
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED)));
        postRepository.save(post);

        if (request.getPublicIds() != null && request.getUrls() != null) {

            if (request.getPublicIds().length != request.getUrls().length) {
                throw new IllegalArgumentException("The size of publicIds and urls must be the same.");
            }
        
            Set<Media> medias = new HashSet<>();
        
            for (int i = 0; i < request.getPublicIds().length; i++) {
                Media media = new Media();
                media.setPublicId(request.getPublicIds()[i]);
                media.setUrl(request.getUrls()[i]);
                media.setPost(post);
        
                medias.add(mediaRepository.save(media));
            }
        
            post.setMedias(medias);
        }
        
        return post;
    }

    // Delete Post
    public void deletePost(Long postId) {
        postRepository.delete(postRepository.findById(postId)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED)));

    }

    // Edit Post
    @Transactional
    public Post editPost(PostRequest request) {
        Post post = postRepository.findById(request.getId())
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        post.setContent(request.getContent());
        post.setPrivacy(request.getPrivacy());

        if(post.getTags() != null) {
            for(Tag tag: post.getTags()) {
                tagService.deleteTag(tag);
            }
    
            Set<Tag> tags = new HashSet<>();
    
            for(String nameTag: request.getNameTag()) {
                Tag tag = tagRepository.findByName(nameTag);
                if(tag != null) 
                    tags.add(tag);
                else
                    tags.add(tagService.createTag(nameTag));
            }
            post.setTags(tags);
        }
        
        post.setUpdatedAt(LocalDateTime.now());
        
        return postRepository.save(post);
    }
}

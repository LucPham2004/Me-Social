package com.me_social.MeSocial.mapper;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.ReelRequest;
import com.me_social.MeSocial.entity.dto.response.ReelResponse;
import com.me_social.MeSocial.entity.modal.Reel;
import com.me_social.MeSocial.repository.CommentRepository;
import com.me_social.MeSocial.repository.LikeRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class ReelMapper {
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    UserRepository userRepository;

    public Reel toReel(ReelRequest request) {
        Reel reel = new Reel();
        reel.setId(request.getPublicId());
        reel.setUser(userRepository.findById(request.getUserId()).get());
        reel.setUrl(request.getUrl());
        reel.setContent(request.getContent());

        return reel;
    }
    
    public ReelResponse toResponse(Reel reel) {
        ReelResponse response = new ReelResponse();

        response.setId(reel.getId());
        response.setContent(reel.getContent());
        response.setUrl(reel.getUrl());
        response.setUserId(reel.getUser().getId());
        response.setCreatedAt(reel.getCreatedAt());
        response.setUpdatedAt(reel.getUpdatedAt());
        response.setCommentNum(commentRepository.countByReelId(reel.getId()));
        response.setLikeNum(likeRepository.countByReelId(reel.getId()));

        return response;
    }
}

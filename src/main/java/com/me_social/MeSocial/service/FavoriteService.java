package com.me_social.MeSocial.service;

import com.me_social.MeSocial.entity.dto.request.FavoriteRequest;
import com.me_social.MeSocial.entity.modal.Favorite;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.FavoriteMapper;
import com.me_social.MeSocial.repository.FavoriteRepository;
import com.me_social.MeSocial.repository.PostRepository;
import com.me_social.MeSocial.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteService {
    FavoriteRepository favoriteRepository;
    FavoriteMapper favoriteMapper;
    UserRepository userRepository;
    PostRepository postRepository;

    public Favorite addPostToFavorite(FavoriteRequest favoriteRequest) {

        if (!userRepository.existsById(favoriteRequest.getUserId()) || !postRepository.existsById(favoriteRequest.getPostId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        if(favoriteRepository.existsByUserIdAndPostId(favoriteRequest.getUserId(), favoriteRequest.getPostId())) {
            throw new AppException(ErrorCode.ENTITY_EXISTED);
        }

        var favorite = favoriteMapper.toFavorite(favoriteRequest);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public void deletePostFromFavorite(Long userId, Long postId) {
        if (!favoriteRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        favoriteRepository.deleteByUserIdAndPostId(userId, postId);
    }
}

package com.me_social.MeSocial.controller;

import com.me_social.MeSocial.entity.dto.request.FavoriteRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.FavoriteResponse;
import com.me_social.MeSocial.entity.modal.Favorite;
import com.me_social.MeSocial.mapper.FavoriteMapper;
import com.me_social.MeSocial.service.FavoriteService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/favorites")
public class FavoriteController {
    FavoriteService favoriteService;
    FavoriteMapper favoriteMapper;

    @PostMapping("/add")
    public ApiResponse<FavoriteResponse> addPostToFavorite (@RequestBody FavoriteRequest favoriteRequest) {
        var favorite = favoriteService.addPostToFavorite(favoriteRequest);
        return ApiResponse.<FavoriteResponse>builder()
                .code(1000)
                .message("Add post with id " + favoriteRequest.getPostId() + " to favorite successfully!")
                .result(favoriteMapper.toFavoriteResponse(favorite))
                .build();
    }

    @DeleteMapping("/{userId}/{postId}")
    public ApiResponse<Void> deletePostFromFavorite (@PathVariable Long userId, @PathVariable Long postId) {
        favoriteService.deletePostFromFavorite(userId, postId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Delete post from favorite successfully!")
                .build();
    }
}

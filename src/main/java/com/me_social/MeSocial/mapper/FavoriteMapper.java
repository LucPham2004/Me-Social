package com.me_social.MeSocial.mapper;

import com.me_social.MeSocial.entity.dto.request.FavoriteRequest;
import com.me_social.MeSocial.entity.dto.response.FavoriteResponse;
import com.me_social.MeSocial.entity.dto.response.PostResponse;
import com.me_social.MeSocial.entity.modal.Favorite;
import com.me_social.MeSocial.repository.PostRepository;
import com.me_social.MeSocial.service.PostService;
import com.me_social.MeSocial.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteMapper {

    UserService userService;
    PostRepository postRepository;
    PostMapper postMapper;

    public Favorite toFavorite(FavoriteRequest favoriteRequest) {
        Favorite favorite = new Favorite();
        favorite.setUser(userService.findById(favoriteRequest.getUserId()));
        favorite.setPost(postRepository.findById(favoriteRequest.getPostId()).get());

        return favorite;
    }

    public FavoriteResponse toFavoriteResponse(Favorite favorite) {
        FavoriteResponse favoriteResponse = new FavoriteResponse();
        favoriteResponse.setId(favorite.getId());
        favoriteResponse.setUserId(favorite.getUser().getId());
        favoriteResponse.setUsername(favorite.getUser().getUsername());

        PostResponse postResponse = new PostResponse();
        var post = favorite.getPost();
        postResponse = postMapper.toPostResponse(post, favorite.getUser().getId());
        favoriteResponse.setPost(postResponse);
        favoriteResponse.setCreatedAt(favorite.getCreatedAt());

        return favoriteResponse;
    }
}

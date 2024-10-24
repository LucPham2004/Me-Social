package com.me_social.MeSocial.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.modal.Media;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.MediaRepository;
import com.me_social.MeSocial.repository.PostRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaService {
    MediaRepository mediaRepository;
    PostRepository postRepository;

    static int MEDIAS_PER_PAGE = 10;

    public Media createMedia(String publicId, String url, Long postId) {
        Media media = new Media();

        media.setUrl(url);
        media.setPublicId(publicId);
        media.setPost(postRepository.findById(postId).get());

        return mediaRepository.save(media);
    }

    public Media getById(String publicId) {
        return mediaRepository.findById(publicId).get();
    }

    public Page<Media> getByPostId(Long postId, int pageNum) {
        if (!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, MEDIAS_PER_PAGE);

        return mediaRepository.findByPostId(postId, pageable);
    }
}

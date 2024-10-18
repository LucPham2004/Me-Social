package com.me_social.MeSocial.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.me_social.MeSocial.entity.dto.request.ReelRequest;
import com.me_social.MeSocial.entity.modal.Reel;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.ReelMapper;
import com.me_social.MeSocial.repository.ReelRepository;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReelService {
    ReelRepository reelRepository;
    UserRepository userRepository;
    ReelMapper mapper;
    
    static int REELS_PER_PAGE = 5;

    // Create Reel
    public Reel createReel(ReelRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Reel reel = mapper.toReel(request);

        return reelRepository.save(reel);
    }

    // Get by id
    public Reel getReelById(String id) {
        return reelRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    // Get all reels paginated
    public Page<Reel> getReelsByUserId(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, REELS_PER_PAGE);
        return reelRepository.findAllByUserId(userId, pageable);
    }

    // Delete Reel
    public void deleteReel(String id) {
        if (!reelRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        reelRepository.deleteById(id);
    }

}

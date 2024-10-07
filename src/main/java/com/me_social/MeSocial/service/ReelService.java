package com.me_social.MeSocial.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.youtube.model.Video;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Reel;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
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
    YouTubeService youTubeService;
    
    static int REELS_PER_PAGE = 10;

    // Create Reel
    public ApiResponse<Reel> createReel(Long userId, MultipartFile file, String content) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        Reel reel = new Reel();
        try {
            reel.setId(youTubeService.uploadVideo(file, user.getFirstName(), content));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        reel.setContent(content);
        reel.setUser(user);
        reel.setCreatedAt(Instant.now());

        ApiResponse<Reel> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Created Reel successfully");
        apiResponse.setResult(reelRepository.save(reel));

        return apiResponse;
    }

    // Get by id
    public ApiResponse<Video> GetReelById(String Id) {
        Reel reel = reelRepository.findById(Id);
        if (reel == null) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        ApiResponse<Video> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Reel successfully");
        try {
            apiResponse.setResult(youTubeService.getVideoById(Id));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }

        return apiResponse;
    }

    //  Get all reels paginated
    public ApiResponse<Page<Reel>> GetReelsByUserId(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, REELS_PER_PAGE);

        ApiResponse<Page<Reel>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get Reels by user successfully");
        apiResponse.setResult(reelRepository.findAllByUserId(userId, pageable));

        return apiResponse;
    }

    public ApiResponse<String> deleteReel(String id) {
        if (reelRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }

        reelRepository.deleteById(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Delete Reel successfully");
        apiResponse.setResult("");

        return apiResponse;
    }
}
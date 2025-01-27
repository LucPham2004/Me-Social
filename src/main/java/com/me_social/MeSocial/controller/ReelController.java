package com.me_social.MeSocial.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.ReelRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.ReelResponse;
import com.me_social.MeSocial.entity.modal.Reel;
import com.me_social.MeSocial.mapper.ReelMapper;
import com.me_social.MeSocial.service.ReelService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class ReelController {
    
    ReelService reelService;
    ReelMapper reelMapper;

    // Create Reel
    @PostMapping
    public ApiResponse<ReelResponse> createReel(@RequestBody ReelRequest request) {
        Reel reel = reelService.createReel(request);
        return ApiResponse.<ReelResponse>builder()
            .code(1000)
            .message("Created Reel successfully")
            .result(reelMapper.toResponse(reel))
            .build();
    }

    // Get Reel by id
    @GetMapping("/{id}")
    public ApiResponse<ReelResponse> getReelById(@PathVariable String id) {
        Reel reel = reelService.getReelById(id);
        return ApiResponse.<ReelResponse>builder()
            .code(1000)
            .message("Get Reel successfully")
            .result(reelMapper.toResponse(reel))
            .build();
    }

    // Get all reels by user
    @GetMapping("/user")
    public ApiResponse<Page<ReelResponse>> getReelsByUserId(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int pageNum) {
        Page<Reel> reels = reelService.getReelsByUserId(userId, pageNum);
        return ApiResponse.<Page<ReelResponse>>builder()
            .code(1000)
            .message("Get Reels by user successfully")
            .result(reels.map(reelMapper::toResponse))
            .build();
    }

    // Delete Reel
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteReel(@PathVariable String id) {
        reelService.deleteReel(id);
        return ApiResponse.<Void>builder()
            .code(1000)
            .message("Delete Reel successfully")
            .build();
    }
}


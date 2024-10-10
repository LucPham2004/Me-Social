package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.modal.Reel;
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

    // Create Reel
    @PostMapping("/upload-video")
    public ApiResponse<Reel> createReel(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long userId,
            @RequestParam String content) {
        Reel reel = reelService.createReel(userId, file, content);
        return ApiResponse.<Reel>builder()
            .code(1000)
            .message("Created Reel successfully")
            .result(reel)
            .build();
    }

    // Get Reel by id
    @GetMapping("/{id}")
    public ApiResponse<Reel> getReelById(@PathVariable String id) {
        Reel reel = reelService.getReelById(id);
        return ApiResponse.<Reel>builder()
            .code(1000)
            .message("Get Reel successfully")
            .result(reel)
            .build();
    }

    // Get all reels by user
    @GetMapping("/user")
    public ApiResponse<Page<Reel>> getReelsByUserId(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int pageNum) {
        Page<Reel> reels = reelService.getReelsByUserId(userId, pageNum);
        return ApiResponse.<Page<Reel>>builder()
            .code(1000)
            .message("Get Reels by user successfully")
            .result(reels)
            .build();
    }

    // Delete Reel
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteReel(@PathVariable String id) {
        reelService.deleteReel(id);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Delete Reel successfully")
            .result("")
            .build();
    }
}


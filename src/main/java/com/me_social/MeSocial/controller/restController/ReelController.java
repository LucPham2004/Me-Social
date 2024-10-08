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

    @PostMapping("/upload-video")
    public ApiResponse<Reel> createReel(@RequestParam("file") MultipartFile file,
                                @RequestParam Long userId,
                                @RequestParam String content) {
        return reelService.createReel(userId, file, content);
    }

    @GetMapping("/{id}")
    public ApiResponse<String> getReelById(@PathVariable String id) {
        return reelService.GetReelById(id);
    }

    @GetMapping("/user")
    public ApiResponse<Page<Reel>> GetReelsByUserId(
        @RequestParam Long userId, 
        @RequestParam(defaultValue = "0") int pageNum) {
        return reelService.GetReelsByUserId(userId, pageNum);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteReel(@PathVariable String id) {
        return reelService.deleteReel(id);
    }
}


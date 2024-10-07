package com.me_social.MeSocial.controller.restController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.youtube.model.Video;
import com.me_social.MeSocial.service.YouTubeService;

@RestController
public class YouTubeController {

    private final YouTubeService youTubeService;

    public YouTubeController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    @PostMapping("/upload-video")
    public String uploadVideo(@RequestParam("file") MultipartFile file,
                              @RequestParam String title,
                              @RequestParam String description) throws Exception {
        return youTubeService.uploadVideo(file, title, description);
    }

    @GetMapping("/get-video")
    public Video getVideoById(@RequestParam String videoId) throws Exception {
        return youTubeService.getVideoById(videoId);
    }
}


package com.me_social.MeSocial.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

@Service
public class YouTubeService {

    @Value("${youtube.apiKey}")
    private String apiKey;

    private final YouTube youtubeService;

    public YouTubeService() throws GeneralSecurityException, IOException {
        youtubeService = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                request -> {}).setApplicationName("youtube-upload").build();
    }

    public String uploadVideo(MultipartFile file, String title, String description) throws IOException {
        // Tạo video snippet (thông tin cơ bản cho video)
        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription(description);
        snippet.setTags(Arrays.asList("Spring Boot", "YouTube API", "Upload"));

        // Cài đặt trạng thái cho video
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public");

        // Tạo đối tượng Video
        Video video = new Video();
        video.setSnippet(snippet);
        video.setStatus(status);

        // Upload video
        YouTube.Videos.Insert videoInsert = youtubeService.videos()
                .insert("snippet,statistics,status", video, new InputStreamContent(
                        "video/*", file.getInputStream()));  // Nhận tệp trực tiếp từ MultipartFile

        Video uploadedVideo = videoInsert.execute();

        return uploadedVideo.getId();  // Trả về ID của video đã tải lên
    }

    public Video getVideoById(String videoId) throws IOException {
        YouTube.Videos.List request = youtubeService.videos()
                .list("snippet,contentDetails,statistics");
        request.setId(videoId);
        request.setKey(apiKey);

        VideoListResponse response = request.execute();
        List<Video> videoList = response.getItems();

        if (videoList != null && !videoList.isEmpty()) {
            return videoList.get(0); 
        } else {
            return null;
        }
    }
}


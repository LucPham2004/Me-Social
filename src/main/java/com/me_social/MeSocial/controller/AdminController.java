package com.me_social.MeSocial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.DashboardSummary;
import com.me_social.MeSocial.service.DashboardService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class AdminController {
    DashboardService service;

    @GetMapping("/summary")
    public ApiResponse<DashboardSummary> getSummary() {
        return ApiResponse.<DashboardSummary>builder()
            .code(1000)
            .message("Get dashboard summary successfully")
            .result(service.getDashboardSummary())
            .build();
    }
}

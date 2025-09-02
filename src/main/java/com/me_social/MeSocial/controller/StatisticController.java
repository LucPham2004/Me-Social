package com.me_social.MeSocial.controller;

import com.me_social.MeSocial.entity.dto.response.StatisticDTO;
import com.me_social.MeSocial.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping
    public StatisticDTO getStatistics() {
        return statisticService.getStatistics();
    }
}

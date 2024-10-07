package com.me_social.MeSocial.service;

import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.response.DashboardSummary;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboadService {
    
    public DashboardSummary getDashboardSummary() {
        
        
        return null;
    }
}

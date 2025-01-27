package com.me_social.MeSocial.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatRequest {
    private List<Long> userIds;
    private String chatName;
    private String chatImage;
}

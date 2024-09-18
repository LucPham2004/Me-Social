package com.me_social.MeSocial.entity.dto.request;

import com.me_social.MeSocial.enums.GroupPrivacy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class GroupCreationRequest {
    Long adminId;
    String name;
    String description;
    GroupPrivacy privacy;
}

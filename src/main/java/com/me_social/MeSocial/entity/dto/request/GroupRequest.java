package com.me_social.MeSocial.entity.dto.request;

import com.me_social.MeSocial.enums.GroupPrivacy;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class GroupRequest {
    Long groupId;
    
    @NotNull
    Long adminId;

    @Size(min=8, message="Group name must be at least 8 charactors")
    String name;

    String description;

    @NotNull
    GroupPrivacy privacy;
}

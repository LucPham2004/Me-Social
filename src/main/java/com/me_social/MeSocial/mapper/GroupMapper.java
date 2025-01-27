package com.me_social.MeSocial.mapper;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.GroupRequest;
import com.me_social.MeSocial.entity.dto.response.GroupResponse;
import com.me_social.MeSocial.entity.modal.Group;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.UserRepository;
import com.me_social.MeSocial.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GroupMapper {
    
    UserRepository userRepository;
    UserService userService;
    GroupRepository groupRepository;

    public Group toGroup(GroupRequest request) {{
        Group group = new Group();

        User user = userService.findById(request.getAdminId());

        if(request.getGroupId() != null) {
            group.setId(request.getGroupId());
        }

        if(!userRepository.existsById(request.getAdminId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        } else {
//            Set<User> members = new HashSet<>();
//            if (group.getMembers() != null) {
//                members = group.getMembers();
//            }
//            members.add(user);
//            group.setMembers(members);

            Set<User> admin = new HashSet<>();
            if(group.getAdmins() != null) 
                admin = group.getAdmins();
            admin.add(user);
            group.setAdmins(admin);
        }

        group.setLocation(request.getLocation());
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setPrivacy(request.getPrivacy());
        group.setImageUrl(request.getImageUrl() != null 
            ? request.getImageUrl() 
            : "https://www.facebook.com/images/groups/groups-default-cover-photo-2x.png");
        
        return group;
    }}

    public GroupResponse toGroupResponse(Group group, Long userId) {
        GroupResponse groupResponse = new GroupResponse();

        if (groupRepository.existsByUserIdInGroup(userId, group.getId()) > 0) {
            int response = groupRepository.existsByUserIdInGroup(userId, group.getId());
            log.info("response : {}", response);
            groupResponse.setJoined(true);
        } else {
            groupResponse.setJoined(false);
        }

        groupResponse.setId(group.getId());
        groupResponse.setName(group.getName());
        groupResponse.setDescription(group.getDescription());
        groupResponse.setPrivacy(group.getPrivacy());
        groupResponse.setImageUrl(group.getImageUrl());
        groupResponse.setCreatedAt(group.getCreatedAt());
        groupResponse.setUpdatedAt(group.getUpdatedAt());
        groupResponse.setLocation(group.getLocation());
        groupResponse.setMemberNum(groupRepository.countMembersById(group.getId()));
        groupResponse.setAdminNum(groupRepository.countAdminsById(group.getId()));
        
        return groupResponse;
    }
}

package com.me_social.MeSocial.mapper;

import java.util.HashSet;
import java.util.Set;

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
public class GroupMapper {
    // public GroupMapper(UserRepository userRepository, GroupRepository groupRepository) {
    //     this.userRepository = userRepository;
    //     this.groupRepository = groupRepository;
    // }
    UserRepository userRepository;
    UserService userService;
    GroupRepository groupRepository;

    public Group toGroup(GroupRequest request) {{
        Group group = new Group();

        if(request.getGroupId() != null) {
            group.setId(request.getGroupId());
        }

        if(!userRepository.existsById(request.getAdminId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        } else {
            Set<User> admin = new HashSet<>();
            if(group.getAdmins() != null) 
                admin = group.getAdmins();
            admin.add(userService.findById(request.getAdminId()).get());
            group.setAdmins(admin);
        }

        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setPrivacy(request.getPrivacy());
        
        return group;
    }}

    public GroupResponse toGroupResponse(Group group) {{
        GroupResponse groupResponse = new GroupResponse();

        groupResponse.setId(group.getId());
        groupResponse.setName(group.getName());
        groupResponse.setDescription(group.getDescription());
        groupResponse.setPrivacy(group.getPrivacy());
        groupResponse.setCreatedAt(group.getCreatedAt());
        groupResponse.setUpdatedAt(group.getUpdatedAt());
        groupResponse.setMemberNum(groupRepository.countMembersById(group.getId()));
        groupResponse.setAdminNum(groupRepository.countAdminsById(group.getId()));
        
        return groupResponse;
    }}
}

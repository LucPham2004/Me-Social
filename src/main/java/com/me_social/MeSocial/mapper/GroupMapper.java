package com.me_social.MeSocial.mapper;

import java.util.Set;
import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.dto.request.GroupCreationRequest;
import com.me_social.MeSocial.entity.dto.response.GroupResponse;
import com.me_social.MeSocial.entity.modal.Group;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class GroupMapper {
    public GroupMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    UserRepository userRepository;

    public Group toGroup(GroupCreationRequest request) {{
        Group group = new Group();

        if(!userRepository.existsById(request.getAdminId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        } else {
            Set<User> admin = new HashSet<>();
            if(group.getAdmins() != null) 
                admin = group.getAdmins();
            admin.add(userRepository.findById(request.getAdminId()));
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
        if(group.getMembers() != null)
            groupResponse.setMemberNum(group.getMembers().size());
        
        return groupResponse;
    }}
}

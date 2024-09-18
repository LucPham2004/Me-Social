package com.me_social.MeSocial.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.request.GroupCreationRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.GroupResponse;
import com.me_social.MeSocial.entity.modal.Group;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.GroupMapper;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.UserRepository;
import com.me_social.MeSocial.utils.PaginationUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class GroupService {
    GroupRepository groupRepository;
    UserRepository userRepository;
    GroupMapper groupMapper;

    static int GROUPS_PER_PAGE = 5;

    // GET
    // Get group by id
    public ApiResponse<GroupResponse> getGroupById(Long id) {
        ApiResponse<GroupResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get group by id successfully");
        apiResponse.setResult(groupMapper.toGroupResponse(groupRepository.findById(id)));

        return apiResponse;
    }

    // Get group by user
    public ApiResponse<Page<GroupResponse>> getGroupByUserId(Long userId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, GROUPS_PER_PAGE);
        ApiResponse<Page<GroupResponse>> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get user's groups successfully");
        apiResponse.setResult(PaginationUtil.convertSetToPage(groupRepository.findByMembersIdOrAdminsId(userId, userId, pageable)
                    .stream()
                    .map(groupMapper::toGroupResponse)
                    .collect(Collectors.toSet()), pageable));

        return apiResponse;
    }

    // POST

    // Create Group
    public ApiResponse<GroupResponse> createGroup(GroupCreationRequest request) {
        if(!userRepository.existsById(request.getAdminId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Group group = groupMapper.toGroup(request);
        group.setCreatedAt(LocalDateTime.now());

        ApiResponse<GroupResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Create group successfully");
        apiResponse.setResult(groupMapper.toGroupResponse(groupRepository.save(group)));

        return apiResponse;
    }

    // Add Admin to Group
    public ApiResponse<String> addAdminToGroup(Long adminId, Long groupId) {
        Group group = groupRepository.findById(groupId);

        Set<User> admins = group.getAdmins();
        admins.add(userRepository.findById(adminId));
        groupRepository.save(group);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Add admin to group successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    // Add member to Group
    public ApiResponse<String> addMemberToGroup(Long memberId, Long groupId) {
        Group group = groupRepository.findById(groupId);

        Set<User> members = group.getMembers();
        members.add(userRepository.findById(memberId));
        groupRepository.save(group);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Add member to group successfully");
        apiResponse.setResult("");

        return apiResponse;
    }
}

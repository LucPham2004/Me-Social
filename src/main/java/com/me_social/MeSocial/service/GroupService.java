package com.me_social.MeSocial.service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.request.GroupRequest;
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
        if(!groupRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        ApiResponse<GroupResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Get group by id successfully");
        apiResponse.setResult(groupMapper.toGroupResponse(groupRepository.findById(id)));

        return apiResponse;
    }

    // Get groups by user
    public ApiResponse<Page<GroupResponse>> getGroupByUserId(Long userId, int pageNum) {
        if(!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
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
    public ApiResponse<GroupResponse> createGroup(GroupRequest request) {
        if(!userRepository.existsById(request.getAdminId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Group group = groupMapper.toGroup(request);
        group.setCreatedAt(LocalDateTime.now());

        ApiResponse<GroupResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Group created successfully");
        apiResponse.setResult(groupMapper.toGroupResponse(groupRepository.save(group)));

        return apiResponse;
    }

    // DELETE
    public ApiResponse<String> deleteGroup(Long groupId) {
        if(!groupRepository.existsById(groupId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        groupRepository.delete(groupRepository.findById(groupId));

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Delete group successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    // PUT
    @Transactional
    public ApiResponse<GroupResponse> editGroup(GroupRequest request) {
        if(!groupRepository.existsById(request.getGroupId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        
        Group group = groupRepository.findById(request.getGroupId());
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setPrivacy(request.getPrivacy());
        group.setUpdatedAt(LocalDateTime.now());

        groupRepository.save(group);

        ApiResponse<GroupResponse> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Delete group successfully");
        apiResponse.setResult(null);

        return apiResponse;
    }

    // Add Admin to Group
    @Transactional
    public ApiResponse<String> addAdminToGroup(Long adminId, Long groupId) {
        if(!groupRepository.existsById(groupId) || !userRepository.existsById(adminId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Group group = groupRepository.findById(groupId);

        Set<User> admins = group.getAdmins();
        admins.add(userRepository.findById(adminId));
        group.setAdmins(admins);
        groupRepository.save(group);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Add admin to group successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    // Remove Group Admin
    @Transactional
    public ApiResponse<String> removeGroupAdmin(Long adminId, Long groupId) {
        if(!groupRepository.existsById(groupId) || !userRepository.existsById(adminId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Group group = groupRepository.findById(groupId);

        Set<User> admins = group.getAdmins();
        admins.remove(userRepository.findById(adminId));
        group.setAdmins(admins);
        groupRepository.save(group);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Remove group Admin successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    // Add member to Group
    @Transactional
    public ApiResponse<String> addMemberToGroup(Long memberId, Long groupId) {
        if(!groupRepository.existsById(groupId) || !userRepository.existsById(memberId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Group group = groupRepository.findById(groupId);

        Set<User> members = group.getMembers();
        members.add(userRepository.findById(memberId));
        group.setMembers(members);
        groupRepository.save(group);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Add member to group successfully");
        apiResponse.setResult("");

        return apiResponse;
    }

    // Remove Group member
    @Transactional
    public ApiResponse<String> removeGroupMember(Long memberId, Long groupId) {
        if(!groupRepository.existsById(groupId) || !userRepository.existsById(memberId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Group group = groupRepository.findById(groupId);

        Set<User> members = group.getMembers();
        members.remove(userRepository.findById(memberId));
        group.setMembers(members);
        groupRepository.save(group);

        ApiResponse<String> apiResponse = new ApiResponse<>();

        apiResponse.setCode(1000);
        apiResponse.setMessage("Remove group member successfully");
        apiResponse.setResult("");

        return apiResponse;
    }
    
}

package com.me_social.MeSocial.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.request.GroupRequest;
import com.me_social.MeSocial.entity.modal.Group;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.GroupMapper;
import com.me_social.MeSocial.repository.GroupRepository;
import com.me_social.MeSocial.repository.UserRepository;

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

    static int GROUPS_PER_PAGE = 20;

    // GET

    // Get group by id
    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
    }

    // Get groups by user
    public Page<Group> getGroupsByUserId(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, GROUPS_PER_PAGE);
        return groupRepository.findByMembersIdOrAdminsId(userId, userId, pageable);
    }

    // Get suggestion groups
    public Page<Group> getSuggestionGroups(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, GROUPS_PER_PAGE);
        return groupRepository.findSuggestedGroups(userId, pageable);
    }

    // POST

    // Create Group
    public Group createGroup(GroupRequest request) {
        if (!userRepository.existsById(request.getAdminId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Group group = groupMapper.toGroup(request);
        group.setCreatedAt(LocalDateTime.now());
        return groupRepository.save(group);
    }

    // POST: Add Admin to Group
    @Transactional
    public void addAdminToGroup(Long adminId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        group.getAdmins().add(admin);
        groupRepository.save(group);
    }

    // POST: Add Member to Group
    @Transactional
    public void addMemberToGroup(Long memberId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        group.getMembers().add(member);
        groupRepository.save(group);
    }
    
    // PUT: Edit Group
    @Transactional
    public Group editGroup(GroupRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setPrivacy(request.getPrivacy());
        group.setUpdatedAt(LocalDateTime.now());
        return groupRepository.save(group);
    }

    // DELETE: Delete Group
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        groupRepository.delete(group);
    }

    // DELETE: Remove Admin from Group
    @Transactional
    public void removeGroupAdmin(Long adminId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        if (group.getAdmins().size() <= 1) {
            throw new AppException(ErrorCode.INVALID_ACTION);
        }
        group.getAdmins().remove(admin);
        groupRepository.save(group);
    }

    // DELETE: Remove Member from Group
    @Transactional
    public void removeGroupMember(Long memberId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));
        group.getMembers().remove(member);
        groupRepository.save(group);
    }
    
}

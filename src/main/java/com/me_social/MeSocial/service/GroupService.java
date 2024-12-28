package com.me_social.MeSocial.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.me_social.MeSocial.entity.modal.Media;
import com.me_social.MeSocial.entity.modal.Post;
import com.me_social.MeSocial.repository.MediaRepository;
import com.me_social.MeSocial.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me_social.MeSocial.entity.dto.request.GroupRequest;
import com.me_social.MeSocial.entity.dto.request.PostRequest;
import com.me_social.MeSocial.entity.modal.Group;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.enums.PostPrivacy;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GroupService {
    private final PostRepository postRepository;
    GroupRepository groupRepository;
    UserRepository userRepository;
    GroupMapper groupMapper;
    PostService postService;
    MediaRepository mediaRepository;

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
    public Page<Group> getSuggestedGroups(Long userId, int pageNum) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.ENTITY_NOT_EXISTED);
        }
        Pageable pageable = PageRequest.of(pageNum, GROUPS_PER_PAGE);
        return groupRepository.findSuggestedGroups(userId, pageable);
    }

    // POST

    // Create Group
    public Group createGroup(GroupRequest request) {
        User admin = userRepository.findById(request.getAdminId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_EXISTED));

        Group group = groupMapper.toGroup(request);
        group.setCreatedAt(LocalDateTime.now());

        group = groupRepository.save(group);

        // Create a post for creating group
        PostRequest postRequest = new PostRequest();
        postRequest.setUserId(request.getAdminId());
        postRequest.setGroupId(group.getId());
        postRequest.setContent(admin.getFirstName() + " " + (admin.getLastName() != null ? admin.getLastName() : " ") + " đã tạo nhóm " + group.getName());
        postRequest.setPrivacy(PostPrivacy.PUBLIC);

        Post post = postService.createPost(postRequest);
        String defaultMediaUrl = "https://www.facebook.com/images/groups/groups-default-cover-photo-2x.png";
        Media media = new Media();
        media.setUrl(defaultMediaUrl);
        media.setPost(post);
        media.setPublicId(defaultMediaUrl);

        Media savedMedia = mediaRepository.save(media);

        Set<Media> medias = new HashSet<>();
        medias.add(savedMedia);

        post.setMedias(medias);

        // Save the post with media
        postRepository.save(post);

        return group;
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

        if (request.getName() != null && !request.getName().isEmpty() && !request.getName().equals(group.getName())) {
            group.setName(request.getName());
        }

        if (request.getDescription() != null && !request.getDescription().isEmpty()
                && !request.getDescription().equals(group.getDescription())) {
            group.setDescription(request.getDescription());
        }

        if (request.getPrivacy() != null && !request.getPrivacy().equals(group.getPrivacy())) {
            group.setPrivacy(request.getPrivacy());
        }

        if (request.getImageUrl() != null && !request.getImageUrl().equals(group.getImageUrl())) {
            group.setImageUrl(request.getImageUrl());
        }

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

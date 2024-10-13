package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.GroupRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.GroupResponse;
import com.me_social.MeSocial.entity.modal.Group;
import com.me_social.MeSocial.mapper.GroupMapper;
import com.me_social.MeSocial.service.GroupService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal=true)
public class GroupController {
    GroupService groupService;
    GroupMapper groupMapper;

    // GET

    // Get group by id
    @GetMapping("/{id}")
    public ApiResponse<GroupResponse> getGroupById(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        return ApiResponse.<GroupResponse>builder()
            .code(1000)
            .message("Get group by id successfully")
            .result(groupMapper.toGroupResponse(group))
            .build();
    }

    // Get groups by user
    @GetMapping("/user/{userId}")
    public ApiResponse<Page<GroupResponse>> getGroupsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int pageNum) {
        Page<Group> groupPage = groupService.getGroupsByUserId(userId, pageNum);
        return ApiResponse.<Page<GroupResponse>>builder()
            .code(1000)
            .message("Get user's groups successfully")
            .result(groupPage.map(groupMapper::toGroupResponse))
            .build();
    }

    // Get groups by user
    @GetMapping("/suggested")
    public ApiResponse<Page<GroupResponse>> getSuggestionGroups(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int pageNum) {
        Page<Group> groupPage = groupService.getSuggestionGroups(userId, pageNum);
        return ApiResponse.<Page<GroupResponse>>builder()
            .code(1000)
            .message("Get suggestion groups successfully")
            .result(groupPage.map(groupMapper::toGroupResponse))
            .build();
    }

    // POST

    // Create Group
    @PostMapping
    public ApiResponse<GroupResponse> createGroup(@Valid @RequestBody GroupRequest request) {
        Group group = groupService.createGroup(request);
        return ApiResponse.<GroupResponse>builder()
            .code(1000)
            .message("Group created successfully")
            .result(groupMapper.toGroupResponse(group))
            .build();
    }

    // Add Admin to Group
    @PostMapping("/admin/{groupId}/{adminId}")
    public ApiResponse<String> addAdminToGroup(@PathVariable Long adminId, @PathVariable Long groupId) {
        groupService.addAdminToGroup(adminId, groupId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Add admin to group successfully")
            .result("")
            .build();
    }

    // Add member to Group
    @PostMapping("/member/{groupId}/{memberId}")
    public ApiResponse<String> addMemberToGroup(@PathVariable Long groupId, @PathVariable Long memberId) {
        groupService.addMemberToGroup(memberId, groupId);
        return ApiResponse.<String>builder()
            .code(1000)
            .message("Add member to group successfully")
            .result("")
            .build();
    }

    // PUT: Edit Group
    @PutMapping
    public ApiResponse<GroupResponse> editGroup(@Valid @RequestBody GroupRequest request) {
        Group group = groupService.editGroup(request);
        return ApiResponse.<GroupResponse>builder()
                .code(1000)
                .message("Edit group successfully")
                .result(groupMapper.toGroupResponse(group))
                .build();
    }

    // DELETE: Delete Group
    @DeleteMapping("/{groupId}")
    public ApiResponse<String> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Delete group successfully")
                .result("")
                .build();
    }

    // DELETE: Remove Admin from Group
    @DeleteMapping("/admin/remove/{groupId}/{adminId}")
    public ApiResponse<String> removeAdminFromGroup(@PathVariable Long adminId, @PathVariable Long groupId) {
        groupService.removeGroupAdmin(adminId, groupId);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Remove admin from group successfully")
                .result("")
                .build();
    }

    // DELETE: Remove Member from Group
    @DeleteMapping("/member/remove/{groupId}/{memberId}")
    public ApiResponse<String> removeMemberFromGroup(@PathVariable Long groupId, @PathVariable Long memberId) {
        groupService.removeGroupMember(memberId, groupId);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Remove member from group successfully")
                .result("")
                .build();
    }

    
}

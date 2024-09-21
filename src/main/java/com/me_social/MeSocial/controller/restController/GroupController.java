package com.me_social.MeSocial.controller.restController;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.GroupRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.GroupResponse;
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

    //GET

    // Get group by id
    @GetMapping("/{id}")
    public ApiResponse<GroupResponse> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }

    // Get group by user
    @GetMapping("/{userId}/{pageNum}")
    public ApiResponse<Page<GroupResponse>> getGroupByUserId(@PathVariable Long userId, @PathVariable int pageNum) {
        return groupService.getGroupByUserId(userId, pageNum);
    }

    // POST

    // Create Group
    @PostMapping("/new")
    public ApiResponse<GroupResponse> createGroup(@Valid @RequestBody GroupRequest request) {
        return groupService.createGroup(request);
    }
    
    // Add Admin to Group
    @PostMapping("/admin/add/{groupId}/{adminId}")
    public ApiResponse<String> addAdminToGroup(@PathVariable Long adminId, @PathVariable Long groupId) {
        return groupService.addAdminToGroup(adminId, groupId);
    }

    // Add member to Group
    @PostMapping("/member/add/{groupId}/{memberId}")
    public ApiResponse<String> addMemberToGroup(@PathVariable Long groupId, @PathVariable Long memberId) {
        return groupService.addMemberToGroup(memberId, groupId);
    }

    // DELETE

    // Delete group
    @DeleteMapping("/{groupId}")
    public ApiResponse<String> deleteGroup(@PathVariable Long groupId) {
        return groupService.deleteGroup(groupId);
    }

    // Remove group Admin
    @DeleteMapping("/admin/remove/{groupId}/{adminId}")
    public ApiResponse<String> removeGroupAdmin(@PathVariable Long adminId, @PathVariable Long groupId) {
        return groupService.removeGroupAdmin(adminId, groupId);
    }

    // Remove group member
    @DeleteMapping("/member/remove/{groupId}/{memberId}")
    public ApiResponse<String> removeGroupMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        return groupService.removeGroupMember(memberId, groupId);
    }
    
    // PUT

    @PutMapping
    public ApiResponse<GroupResponse> editGroup(@Valid @RequestBody GroupRequest request) {
        return groupService.editGroup(request);
    }

    
}

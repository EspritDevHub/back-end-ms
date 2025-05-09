package com.example.groupmanagmenetms.controller;

import com.example.groupmanagmenetms.DTO.UserResponseDTO;
import com.example.groupmanagmenetms.entity.Group;
import com.example.groupmanagmenetms.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // Create a group
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.ok(createdGroup);
    }

    // Get all groups
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    // Get group by ID
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable String id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update group
    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable String id, @RequestBody Group group) {
        try {
            Group updatedGroup = groupService.updateGroup(id, group);
            return ResponseEntity.ok(updatedGroup);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete group
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<Group> addMember(@PathVariable String groupId, @RequestBody UserResponseDTO member) {
        Group updatedGroup = groupService.addMemberToGroup(groupId, member);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{groupId}/remove-member/{memberId}")
    public ResponseEntity<Group> removeMember(@PathVariable String groupId, @PathVariable String memberId) {
        Group updatedGroup = groupService.removeMemberFromGroup(groupId, memberId);
        return ResponseEntity.ok(updatedGroup);
    }

    @GetMapping("/by-member/{memberId}")
    public List<Group> getGroupsByMember(@PathVariable String memberId) {
        return groupService.getGroupsByMemberId(memberId);
    }


}

package com.example.groupmanagmenetms.service;

import com.example.groupmanagmenetms.DTO.UserResponseDTO;
import com.example.groupmanagmenetms.entity.Group;
import com.example.groupmanagmenetms.repository.IGroupRepositroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupService {

    private final IGroupRepositroy repository;

    // Create
    public Group createGroup(Group group) {
        validateEncadrantRole(group);
        return repository.save(group);
    }

    // Read all
    public List<Group> getAllGroups() {
        return repository.findAll();
    }

    // Read by ID
    public Optional<Group> getGroupById(String id) {
        return repository.findById(id);
    }

    // Update
    public Group updateGroup(String id, Group updatedGroup) {
        validateEncadrantRole(updatedGroup);
        return repository.findById(id).map(existingGroup -> {
            existingGroup.setName(updatedGroup.getName());
            existingGroup.setProjectName(updatedGroup.getProjectName());
            existingGroup.setEncadrant(updatedGroup.getEncadrant());
            existingGroup.setMembers(updatedGroup.getMembers());
            return repository.save(existingGroup);
        }).orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
    }

    // Delete
    public void deleteGroup(String id) {
        repository.deleteById(id);
    }
    public Group addMemberToGroup(String groupId, UserResponseDTO newMember) {
        return repository.findById(groupId).map(group -> {
            List<UserResponseDTO> members = group.getMembers();
            if (members == null) {
                members = new ArrayList<>();
            }

            boolean alreadyMember = members.stream()
                    .anyMatch(member -> member.getId().equals(newMember.getId()));

            if (!alreadyMember) {
                members.add(newMember);
                group.setMembers(members);
                return repository.save(group);
            } else {
                throw new IllegalArgumentException("User is already a member of the group.");
            }
        }).orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
    }

    public Group removeMemberFromGroup(String groupId, String memberId) {
        return repository.findById(groupId).map(group -> {
            List<UserResponseDTO> members = group.getMembers();
            if (members != null && !members.isEmpty()) {
                boolean removed = members.removeIf(member -> member.getId().equals(memberId));
                if (removed) {
                    group.setMembers(members);
                    return repository.save(group);
                } else {
                    throw new IllegalArgumentException("Member not found in the group.");
                }
            } else {
                throw new IllegalStateException("Group has no members to remove.");
            }
        }).orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
    }
    public List<Group> getGroupsByMemberId(String memberId) {
        return repository.findByMembers_Id(memberId);
    }
    private void validateEncadrantRole(Group group) {
        if (group.getEncadrant() == null || !"TEACHER".equalsIgnoreCase(group.getEncadrant().getRole().name())) {
            throw new IllegalArgumentException("Encadrant must have the role 'TEACHER'");
        }
    }
}

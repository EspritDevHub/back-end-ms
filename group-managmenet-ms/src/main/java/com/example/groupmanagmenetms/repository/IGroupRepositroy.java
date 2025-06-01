package com.example.groupmanagmenetms.repository;

import com.example.groupmanagmenetms.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IGroupRepositroy extends MongoRepository<Group, String> {
    List<Group> findByMembers_Id(String memberId);
    List<Group> findByIsMembersNotifiedFalse();
}

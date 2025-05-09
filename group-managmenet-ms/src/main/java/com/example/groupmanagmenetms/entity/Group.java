package com.example.groupmanagmenetms.entity;

import com.example.groupmanagmenetms.DTO.UserResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.PrePersist;
import java.util.List;
import java.util.UUID;

@Document(collection = "groups")
@Getter
@Setter
@AllArgsConstructor
public class Group {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String name;
    private String projectName;
    private UserResponseDTO encadrant;
    private List<UserResponseDTO> members;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }
}

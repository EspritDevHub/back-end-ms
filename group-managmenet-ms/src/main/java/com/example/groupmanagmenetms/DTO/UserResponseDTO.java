package com.example.groupmanagmenetms.DTO;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private Roles role;
    private String phone;
    private String className;
    private String espritId;
}

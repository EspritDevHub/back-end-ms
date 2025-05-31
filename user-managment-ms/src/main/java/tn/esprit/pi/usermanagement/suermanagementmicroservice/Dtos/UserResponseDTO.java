package tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos;

import lombok.Data;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.Roles;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private Roles role;
    private String phone;
    private String className;
    private Boolean is2FAEnabled;
    private String token;
    private String espritId;
    private boolean active;

}

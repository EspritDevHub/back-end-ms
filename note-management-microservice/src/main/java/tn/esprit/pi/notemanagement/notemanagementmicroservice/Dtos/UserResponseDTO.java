package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;


import lombok.Data;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.Roles;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private Roles role;
    private String phone;
    private Boolean is2FAEnabled;
    private String token;
}

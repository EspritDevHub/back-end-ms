package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;


import lombok.Data;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.Roles;

@Data
public class UserResponseDTO {
    private String id;
    private String name;
    private String Groupe;
    private String phone;

}

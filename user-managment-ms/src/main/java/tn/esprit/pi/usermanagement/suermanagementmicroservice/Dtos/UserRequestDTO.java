package tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos;
import lombok.Data;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.Roles;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private Roles role;
    private String phone;
}

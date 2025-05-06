package tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign;

import org.springframework.stereotype.Component;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.UserResponseDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.Roles;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserResponseDTO getUserById(String id) {
        // Ici, vous pouvez définir un comportement par défaut en cas de défaillance
        UserResponseDTO fallbackResponse = new UserResponseDTO();
        fallbackResponse.setId("1");
        fallbackResponse.setName("Sana");
        fallbackResponse.setGroupe("Groupe 1");
        fallbackResponse.setPhone("0000000000");


        return fallbackResponse;
    }



    @Override
    public String getUserRole(String id) {
        return "ADLIN";
    }
}


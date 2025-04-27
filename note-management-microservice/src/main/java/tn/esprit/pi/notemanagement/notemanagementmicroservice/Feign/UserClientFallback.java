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
        fallbackResponse.setId(id);
        fallbackResponse.setName("Sana");
        fallbackResponse.setEmail("unknown@fallback.com");
        fallbackResponse.setRole(Roles.STUDENT); // Par exemple, retourner un rôle par défaut
        fallbackResponse.setPhone("0000000000");
        fallbackResponse.setIs2FAEnabled(false);
        fallbackResponse.setToken("");

        return fallbackResponse;
    }



    @Override
    public String getUserRole(String id) {
        return "ADLIN";
    }
}


package tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.UserResponseDTO;

@FeignClient(name = "user-service",url ="http://localhost:9090/users/", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponseDTO getUserById(@PathVariable String id);

    @GetMapping("/api/users/{id}/role")
    String getUserRole(@PathVariable String id);
}



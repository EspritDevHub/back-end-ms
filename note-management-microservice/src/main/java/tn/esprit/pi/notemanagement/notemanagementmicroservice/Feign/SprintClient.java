package tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SprintDTO;

import java.util.List;

@FeignClient(name = "sprint-service", url = "localhost:9098/api/criteres/sprints", fallback = SprintClientFallback.class)
@Qualifier("sprintClient")
public interface SprintClient {

    @GetMapping("/sprints")
    List<SprintDTO> getSprints();
}
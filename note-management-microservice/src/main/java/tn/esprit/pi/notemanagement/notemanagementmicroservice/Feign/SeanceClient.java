package tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;

import java.util.List;

@FeignClient(name = "SEANCE-SERVICE", url = "http://localhost:8089", fallback = SeanceClientFallback.class)
public interface SeanceClient {

    @GetMapping("/api/seances/{id}")
    SeanceDTO getSeanceById(@PathVariable("id") String id);

    @GetMapping("/api/seances/sprint/{sprintId}")
    List<SeanceDTO> getSeancesBySprint(@PathVariable String sprintId);

    @PutMapping("/api/seances/{id}")
    SeanceDTO updateSeance(@PathVariable("id") String id, @RequestBody SeanceDTO seance);
}

package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.ClassementResponse;

import java.util.ArrayList;
import java.util.List;

// 3. Controller Spring Boot
@RestController
@RequestMapping("/api/classement")
public class DashboardController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/top-etudiants")
    public ResponseEntity<ClassementResponse> getClassement() {
        String url = "http://localhost:5005/process";
        ClassementResponse response = restTemplate.getForObject(url, ClassementResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reussite")
    public ResponseEntity<List<ReussiteResponse>> getPourcentageReussite() {
        String url = "http://localhost:5005/process";
        ClassementResponse response = restTemplate.getForObject(url, ClassementResponse.class);

        List<ReussiteResponse> stats = new ArrayList<>();

        for (ClassementResponse.SeanceClassement seance : response.classement) {
            double maxNote = 20.0;
            double moyenne = seance.topEtudiants.stream().mapToDouble(e -> e.valeur).average().orElse(0);
            double pourcentage = (moyenne / maxNote) * 100;
            String badge = pourcentage >= 75 ? "🏅" : "🎓";

            ReussiteResponse r = new ReussiteResponse();
            r.seanceId = seance.seanceId;
            r.seanceTitre = seance.seanceTitre;
            r.pourcentageReussite = pourcentage;
            r.badge = badge;
            stats.add(r);
        }
        return ResponseEntity.ok(stats);
    }

    static class ReussiteResponse {
        public String seanceId;
        public String seanceTitre;
        public double pourcentageReussite;
        public String badge;
    }
}
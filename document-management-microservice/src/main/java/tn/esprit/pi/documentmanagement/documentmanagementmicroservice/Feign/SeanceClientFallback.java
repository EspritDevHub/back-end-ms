package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.SeanceDTO;


import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class SeanceClientFallback implements tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Feign.SeanceClient
{

    private static final Logger logger = LoggerFactory.getLogger(SeanceClientFallback.class);

    @Override
    public SeanceDTO getSeanceById(String id) {
        logger.error("Fallback triggered: Unable to retrieve Seance with ID " + id);
        return SeanceDTO.builder()
                .id("2") // default ID for fallback
                .titre("Séance fallback")
                .description("Séance par défaut car service indisponible")

                .build();
    }

    @Override
    public List<SeanceDTO> getSeancesBySprint(String sprintId) {
        logger.error("Fallback triggered: Unable to retrieve Seances for Sprint ID " + sprintId);
        return Collections.emptyList(); // Return an empty list as a fallback
    }

    @Override
    public SeanceDTO updateSeance(String id, SeanceDTO seance) {
        logger.error("Fallback triggered: Unable to update Seance with ID " + id);
        return null; // Return null or any appropriate default value
    }
}

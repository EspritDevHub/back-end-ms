package tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class SeanceClientFallback implements SeanceClient {

    private static final Logger logger = LoggerFactory.getLogger(SeanceClientFallback.class);

    @Override
    public SeanceDTO getSeanceById(String id) {
        logger.error("Fallback triggered: Unable to retrieve Seance with ID " + id);
        return SeanceDTO.builder()
                .id("2") // default ID for fallback
                .titre("Séance fallback")
                .description("Séance par défaut car service indisponible")
                .Numero(0)
                .Note(0)
                .sprintId("1")
                .critereIds(Collections.emptyList())
                .date(new Date())
                .typeNote(TypeNote.INDIVIDUELLE)
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

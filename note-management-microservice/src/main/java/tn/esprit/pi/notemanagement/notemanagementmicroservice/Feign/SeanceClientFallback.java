package tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign;


import org.springframework.stereotype.Component;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class SeanceClientFallback implements SeanceClient {

    @Override
    public SeanceDTO getSeanceById(String id) {
        // Ici tu retournes un SeanceDTO par défaut (existant)
        return SeanceDTO.builder()
                .id(id)
                .titre("Séance fallback")
                .description("Séance par défaut car service indisponible")
                .Numero(0)
                .Note(0)
                .sprintId(null)
                .critereIds(Collections.emptyList())
                .date(new Date())
                .typeNote(TypeNote.INDIVIDUELLE)
                .build();
    }

    @Override
    public List<SeanceDTO> getSeancesBySprint(String sprintId) {
        return List.of();
    }
}


package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Mappers;


import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Dtos.SeanceNoteDTO;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Entities.SeanceNote;

public class SeanceNoteMapper {

    public static SeanceNoteDTO toDto(SeanceNote s) {
        return SeanceNoteDTO.builder()
                .id(s.getId())
                .titre(s.getTitre())
                .description(s.getDescription())
                .numero(s.getNumero())
                .note(s.getNote())
                .sprintId(s.getSprintId())
                .critereIds(s.getCritereIds())
                .date(s.getDate())
                .typeNote(s.getTypeNote())
                .build();
    }

    public static SeanceNote toEntity(SeanceNoteDTO dto) {
        return SeanceNote.builder()
                .id(dto.getId())
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .numero(dto.getNumero())
                .note(dto.getNote())
                .sprintId(dto.getSprintId())
                .critereIds(dto.getCritereIds())
                .date(dto.getDate())
                .typeNote(dto.getTypeNote())
                .build();
    }
}


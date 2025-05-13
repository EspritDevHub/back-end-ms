package tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Mappers;


import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Entities.Seance;

public class SeanceMapper {

    public static SeanceDTO toDto(Seance s) {
        return SeanceDTO.builder()
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

    public static Seance toEntity(SeanceDTO dto) {
        return Seance.builder()
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


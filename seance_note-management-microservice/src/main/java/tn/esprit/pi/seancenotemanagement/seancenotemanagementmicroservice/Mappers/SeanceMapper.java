package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Mappers;


import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Entities.Seance;

public class SeanceMapper {

    public static SeanceDTO toDto(Seance s) {
        return SeanceDTO.builder()
                .id(s.getId())
                .titre(s.getTitre())
                .description(s.getDescription())
                .Numero(s.getNumero())
                .Note(s.getNote())
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
                .Numero(dto.getNumero())
                .Note(dto.getNote())
                .sprintId(dto.getSprintId())
                .critereIds(dto.getCritereIds())
                .date(dto.getDate())
                .typeNote(dto.getTypeNote())
                .build();
    }
}


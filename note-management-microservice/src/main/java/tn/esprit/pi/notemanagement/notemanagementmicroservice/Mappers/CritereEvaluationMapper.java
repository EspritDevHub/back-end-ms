package tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers;

import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;

public class CritereEvaluationMapper {

    public static CritereEvaluationDTO toDto(CritereEvaluation entity) {
        return CritereEvaluationDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .description(entity.getDescription())
                .coefficient(entity.getCoefficient())
                .sprintId(entity.getSprintId())
                .build();
    }

    public static CritereEvaluation toEntity(CritereEvaluationDTO dto) {
        return CritereEvaluation.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .coefficient(dto.getCoefficient())
                .sprintId(dto.getSprintId())
                .build();
    }
}

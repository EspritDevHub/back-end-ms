package tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers;


import org.springframework.stereotype.Component;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationRequestDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationResponseDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;

@Component
public class CritereEvaluationMapper {

    public CritereEvaluation toEntity(CritereEvaluationRequestDTO dto) {
        return CritereEvaluation.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .coefficient(dto.getCoefficient())
                .sprintId(dto.getSprintId())
                .build();
    }

    public CritereEvaluationResponseDTO toDTO(CritereEvaluation entity) {
        return CritereEvaluationResponseDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .description(entity.getDescription())
                .coefficient(entity.getCoefficient())
                .sprintId(entity.getSprintId())
                .build();
    }
}

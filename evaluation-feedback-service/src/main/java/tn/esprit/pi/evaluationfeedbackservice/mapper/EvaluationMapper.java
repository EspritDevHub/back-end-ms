package tn.esprit.pi.evaluationfeedbackservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.pi.evaluationfeedbackservice.dto.EvaluationDto;
import tn.esprit.pi.evaluationfeedbackservice.entity.Evaluation;

@Component
public class EvaluationMapper {

    public static EvaluationDto toDto(Evaluation evaluation) {
        if (evaluation == null) return null;

        return EvaluationDto.builder()
                .id(evaluation.getId())
                .description(evaluation.getDescription())
                .note(evaluation.getNote())
                .critere(evaluation.getCritere())
                .projet(evaluation.getProjet())
                .user(evaluation.getUser())
                .build();
    }

    public static Evaluation toEntity(EvaluationDto dto) {
        if (dto == null) return null;

        return Evaluation.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .note(dto.getNote())
                .critere(dto.getCritere())
                .projet(dto.getProjet())
                .user(dto.getUser())
                .build();
    }
}

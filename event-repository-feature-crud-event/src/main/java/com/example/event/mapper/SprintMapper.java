package com.example.event.mapper;

import com.example.event.dto.SprintDTO;
import com.example.event.model.Sprint;
import com.example.event.repository.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SprintMapper {

    private final PhaseRepository phaseRepository;

    @Autowired
    public SprintMapper(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    public SprintDTO toDTO(Sprint entity) {
        if (entity == null) return null;

        SprintDTO dto = new SprintDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPhaseId(entity.getPhaseId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setActive(entity.getIsActive());

        // Récupérer le nom de la phase
        if (entity.getPhaseId() != null) {
            phaseRepository.findById(entity.getPhaseId())
                    .ifPresent(phase -> dto.setPhaseName(phase.getName()));
        }

        return dto;
    }

    public Sprint toEntity(SprintDTO dto) {
        if (dto == null) return null;

        Sprint entity = new Sprint();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setPhaseId(dto.getPhaseId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setStatus(dto.getStatus());
        entity.setIsActive(dto.getActive());

        return entity;
    }

    public List<SprintDTO> toDTOList(List<Sprint> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Sprint> toEntityList(List<SprintDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

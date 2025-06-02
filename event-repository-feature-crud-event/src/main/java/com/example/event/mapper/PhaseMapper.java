package com.example.event.mapper;

import com.example.event.dto.PhaseDTO;
import com.example.event.model.Phase;

import java.util.List;
import java.util.stream.Collectors;


public class PhaseMapper {

    public static PhaseDTO toDTO(Phase entity) {
        if (entity == null) return null;

        PhaseDTO dto = new PhaseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setIsActive(entity.getIsActive());

        return dto;
    }

    public static Phase toEntity(PhaseDTO dto) {
        if (dto == null) return null;

        Phase entity = new Phase();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIsActive(dto.getIsActive());

        return entity;
    }

    public static List<PhaseDTO> toDTOList(List<Phase> entities) {
        return entities.stream()
                .map(PhaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Phase> toEntityList(List<PhaseDTO> dtos) {
        return dtos.stream()
                .map(PhaseMapper::toEntity)
                .collect(Collectors.toList());
    }
}
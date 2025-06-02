package com.example.event.mapper;

import com.example.event.dto.SprintDTO;
import com.example.event.model.Sprint;

import java.util.List;
import java.util.stream.Collectors;

public class SprintMapper {

    public static SprintDTO toDTO(Sprint entity) {
        if (entity == null) return null;

        SprintDTO dto = new SprintDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPhaseId(entity.getPhaseId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setActive(entity.getIsActive());

        return dto;
    }

    public static Sprint toEntity(SprintDTO dto) {
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

    public static List<SprintDTO> toDTOList(List<Sprint> entities) {
        return entities.stream()
                .map(SprintMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Sprint> toEntityList(List<SprintDTO> dtos) {
        return dtos.stream()
                .map(SprintMapper::toEntity)
                .collect(Collectors.toList());
    }
}


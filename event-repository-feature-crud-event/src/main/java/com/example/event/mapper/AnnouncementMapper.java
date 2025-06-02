package com.example.event.mapper;

import com.example.event.dto.AnnouncementDTO;
import com.example.event.model.Announcement;

public class AnnouncementMapper {

    public static AnnouncementDTO toDTO(Announcement entity) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public static Announcement toEntity(AnnouncementDTO dto) {
        Announcement entity = new Announcement();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return entity;
    }
}

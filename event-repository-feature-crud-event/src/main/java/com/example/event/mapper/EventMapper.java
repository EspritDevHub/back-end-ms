package com.example.event.mapper;

import com.example.event.dto.EventDTO;
import com.example.event.model.Event;

public class EventMapper {

    public static Event toEntity(EventDTO dto) {
        if (dto == null) {
            return null;
        }

        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setEventType(dto.getEventType());
        event.setLocation(dto.getLocation());
        event.setOrganizer(dto.getOrganizer());
        event.setIsActive(dto.getIsActive());
        event.setProjectId(dto.getProjectId());
        return event;
    }

    public static EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }

        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setStartDate(event.getStartDate());
        dto.setEndDate(event.getEndDate());
        dto.setEventType(event.getEventType());
        dto.setLocation(event.getLocation());
        dto.setOrganizer(event.getOrganizer());
        dto.setIsActive(event.getIsActive());
        dto.setProjectId(event.getProjectId());
        return dto;
    }
}

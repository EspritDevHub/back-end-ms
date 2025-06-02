package com.example.event.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnouncementDTO {
    private String id;
    private String title;
    private String content;
    private Boolean isActive;
}

package com.example.event.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sprints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sprint {
    @Id
    private String id;
    private String title;
    private String phaseId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private Boolean isActive;
}
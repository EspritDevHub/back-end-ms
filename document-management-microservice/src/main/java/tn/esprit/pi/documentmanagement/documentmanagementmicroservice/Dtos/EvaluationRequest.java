package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;


import lombok.Data;

@Data
public class EvaluationRequest {
    private EvaluationDto evaluationDto;
    private DocumentDto documentDto;
}


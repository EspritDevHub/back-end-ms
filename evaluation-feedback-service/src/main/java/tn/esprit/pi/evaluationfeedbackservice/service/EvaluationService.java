package tn.esprit.pi.evaluationfeedbackservice.service;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi.evaluationfeedbackservice.dto.EvaluationDto;
import tn.esprit.pi.evaluationfeedbackservice.entity.Critere;
import tn.esprit.pi.evaluationfeedbackservice.entity.Evaluation;
import tn.esprit.pi.evaluationfeedbackservice.mapper.EvaluationMapper;
import tn.esprit.pi.evaluationfeedbackservice.repository.EvaluationRepository;


import java.util.*;

@Service
public class EvaluationService {
    private final EvaluationRepository repository;
    private final MongoTemplate mongoTemplate;
    private final EvaluationMapper evaluationMapper;
    private  final EmailService emailService;

    public EvaluationService(EvaluationRepository repository, MongoTemplate mongoTemplate, EvaluationMapper evaluationMapper, EmailService emailService) {
        this.emailService = emailService;
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
        this.evaluationMapper = evaluationMapper;
    }

    public List<EvaluationDto> getAll(Long id) {
        return repository.findByProjet(id).stream().map(
                EvaluationMapper::toDto).toList() ;
    }

    public EvaluationDto addOrUpdateEvaluation(Evaluation newEvaluation) {
        // 1. Validate note
        if (newEvaluation.getNote() == null || newEvaluation.getNote() < 1 || newEvaluation.getNote() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Note must be between 1 and 5.");
        }

        // 2. Check if evaluation already exists
        Optional<Evaluation> existingEvalOpt = repository.findByProjetAndUserAndCritere(
                newEvaluation.getProjet(),
                newEvaluation.getUser(),
                newEvaluation.getCritere()
        );

        Evaluation savedEvaluation;
        if (existingEvalOpt.isPresent()) {
            Evaluation existing = existingEvalOpt.get();
            existing.setNote(newEvaluation.getNote());
            existing.setDescription(newEvaluation.getDescription());
            savedEvaluation = repository.save(existing);
        } else {
            savedEvaluation = repository.save(newEvaluation);
        }

        // 3. Count evaluations for the project
        Long projectId = savedEvaluation.getProjet();
        long evalCount = repository.countByProjet(projectId);

        // 4. If at least 10 evaluations, check the average note
        if (evalCount >= 5) {
            Double avgNote = repository.averageNoteByProjet(projectId);
            if (avgNote != null && avgNote < 2.5) {
                System.out.println("The given project should be reviewed ==> "+projectId);
                emailService.sendLowRatingAlertToAdmin(savedEvaluation.getProjet(), avgNote);
            }
        }

        return EvaluationMapper.toDto(savedEvaluation);
    }




    public void deleteEvaluation(String id) {
        repository.deleteById(id);
    }


    public List<EvaluationDto> getEvaluationsByProject(String projectId, Critere critere) {
        Long projetId;

        try {
            projetId = Long.parseLong(projectId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid projectId format. Must be numeric.");
        }

        if (!repository.existsByProjet(projetId)) {
            throw new NoSuchElementException("Project with ID " + projectId + " not found in any evaluation.");
        }

        if (critere != null) {
            return repository.findByProjetAndCritere(projetId, critere).stream().map(EvaluationMapper::toDto).toList();
        } else {
            return repository.findByProjet(projetId).stream().map(EvaluationMapper::toDto).toList();
        }
    }

    public Map<String, Object> getAverageStatsByProject(Long projectId) {
        // Match evaluations for a specific project
        MatchOperation match = Aggregation.match(Criteria.where("projet").is(projectId));

        // Group by critere and calculate average note
        GroupOperation groupByCritere = Aggregation.group("critere").avg("note").as("average");

        AggregationResults<Document> critereResults = mongoTemplate.aggregate(
                Aggregation.newAggregation(match, groupByCritere),
                "evaluation",
                Document.class
        );

        // Extract results
        Map<String, Double> averageByCritere = new HashMap<>();
        for (Document doc : critereResults.getMappedResults()) {
            averageByCritere.put(doc.getString("_id"), doc.getDouble("average"));
        }

        // Get the global average for the project
        GroupOperation globalGroup = Aggregation.group().avg("note").as("globalAverage");
        AggregationResults<Document> globalResult = mongoTemplate.aggregate(
                Aggregation.newAggregation(match, globalGroup),
                "evaluation",
                Document.class
        );

        Double globalAverage = globalResult.getUniqueMappedResult() != null
                ? globalResult.getUniqueMappedResult().getDouble("globalAverage")
                : null;

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("projectId", projectId);
        response.put("globalAverage", globalAverage);
        response.put("averageByCritere", averageByCritere);

        return response;
    }

}

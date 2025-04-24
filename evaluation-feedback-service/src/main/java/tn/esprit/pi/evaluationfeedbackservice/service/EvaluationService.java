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
import tn.esprit.pi.evaluationfeedbackservice.entity.Critere;
import tn.esprit.pi.evaluationfeedbackservice.entity.Evaluation;
import tn.esprit.pi.evaluationfeedbackservice.repository.EvaluationRepository;


import java.util.*;

@Service
public class EvaluationService {
    private final EvaluationRepository repository;
    private final MongoTemplate mongoTemplate;

    public EvaluationService(EvaluationRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Evaluation> getAll() {
        return repository.findAll();
    }

    public Evaluation addOrUpdateEvaluation(Evaluation newEvaluation) {
        // 1. Validate note
        if (newEvaluation.getNote() == null || newEvaluation.getNote() < 1 || newEvaluation.getNote() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Note must be between 1 and 5.");
        }

        // 2. Check if an evaluation already exists for this user, project, and critere
        Optional<Evaluation> existingEvalOpt = repository.findByProjetAndUserAndCritere(
                newEvaluation.getProjet(),
                newEvaluation.getUser(),
                newEvaluation.getCritere()
        );

        if (existingEvalOpt.isPresent()) {
            Evaluation existing = existingEvalOpt.get();
            existing.setNote(newEvaluation.getNote());
            existing.setDescription(newEvaluation.getDescription());
            return repository.save(existing);
        }

        // 3. Else, create a new evaluation
        return repository.save(newEvaluation);
    }


    public void deleteEvaluation(String id) {
        repository.deleteById(id);
    }

    public List<Evaluation> getEvaluationsByProject(String projectId, Critere critere) {
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
            return repository.findByProjetAndCritere(projetId, critere);
        } else {
            return repository.findByProjet(projetId);
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

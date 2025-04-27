package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ICritereEvaluationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CritereEvaluationService {
    private final ICritereEvaluationRepository critereRepo;

    public CritereEvaluation create(CritereEvaluation critere) {
        return critereRepo.save(critere);
    }

    public List<CritereEvaluation> getAll() {
        return critereRepo.findAll();
    }

    public Optional<CritereEvaluation> get(String id) {
        return critereRepo.findById(id);
    }

    public CritereEvaluation update(String id, CritereEvaluation critere) {
        critere.setId(id);
        return critereRepo.save(critere);
    }

    public void delete(String id) {
        critereRepo.deleteById(id);
    }

    public void deleteAll() {
        critereRepo.deleteAll();
    }
}


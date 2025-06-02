package com.example.event.service;

import com.example.event.model.Phase;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPhaseService {

    Phase create(Phase phase);
    Phase update(Phase phase);
    void delete(String id);
    List<Phase> getAll();
    Optional<Phase> getById(String id);
    Map<String, Object> getDashboardSummary();
    double getPhaseProgress(String phaseId);

}

package com.example.event.service;

import com.example.event.model.Phase;
import com.example.event.model.Sprint;

import java.util.List;
import java.util.Optional;

public interface ISprintService {

    Sprint create(Sprint sprint);
    Sprint update(Sprint sprint);
    void delete(String id);
    List<Sprint> getByPhaseId(String phaseId);
    List<Sprint> getAll();
    Optional<Sprint> getById(String id);
    List<Sprint> getCurrentSprints();

}

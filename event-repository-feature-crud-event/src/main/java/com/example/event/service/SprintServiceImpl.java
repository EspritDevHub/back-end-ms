package com.example.event.service;

import com.example.event.model.Sprint;
import com.example.event.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements ISprintService {

    private final SprintRepository sprintRepository;

    @Override
    public Sprint create(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    @Override
    public Sprint update(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    @Override
    public void delete(String id) {
        sprintRepository.deleteById(id);
    }

    @Override
    public List<Sprint> getByPhaseId(String phaseId) {
        return sprintRepository.findByPhaseId(phaseId);
    }

    @Override
    public List<Sprint> getAll() {
        return sprintRepository.findAll();
    }
    @Override
    public Optional<Sprint> getById(String id) {
        return sprintRepository.findById(id);
    }
    @Override
    public List<Sprint> getCurrentSprints() {
        LocalDateTime now = LocalDateTime.now();
        return sprintRepository.findAll().stream()
                .filter(s -> s.getStartDate().isBefore(now) && s.getEndDate().isAfter(now))
                .toList();
    }

}

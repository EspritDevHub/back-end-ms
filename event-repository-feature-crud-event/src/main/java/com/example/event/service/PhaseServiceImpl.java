package com.example.event.service;

import com.example.event.model.Announcement;
import com.example.event.model.Event;
import com.example.event.model.Phase;
import com.example.event.model.Sprint;
import com.example.event.repository.AnnouncementRepository;
import com.example.event.repository.EventRepository;
import com.example.event.repository.PhaseRepository;
import com.example.event.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements IPhaseService {
    private final PhaseRepository repository;
    private final EventRepository eventRepository;
    private final AnnouncementRepository announcementRepository;
    private final ISprintService iSprintService;
    private final SprintRepository sprintRepository;

    @Override
    public Phase create(Phase phase) {
        phase.setIsActive(true);
        return repository.save(phase);
    }

    @Override
    public Phase update(Phase phase) {
        return repository.save(phase);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Phase> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Phase> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        summary.put("activePhases", repository.findAll().stream().filter(Phase::getIsActive).count());
        summary.put("ongoingSprints", iSprintService.getCurrentSprints().size());
        summary.put("upcomingEvents", eventRepository.findAll().stream()
                .filter(e -> e.getStartDate().isAfter(LocalDateTime.now())).count());
        summary.put("activeAnnouncements", announcementRepository.findAll().stream()
                .filter(Announcement::getIsActive).count());

        return summary;
    }

    @Override
    public double getPhaseProgress(String phaseId) {
        List<Sprint> sprints = sprintRepository.findByPhaseId(phaseId);
        if (sprints.isEmpty()) return 0.0;

        long completed = sprints.stream().filter(s -> "COMPLETED".equalsIgnoreCase(s.getStatus())).count();
        return (completed * 100.0) / sprints.size();
    }


}
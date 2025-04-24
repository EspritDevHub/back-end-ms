package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationRequestDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationResponseDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.CritereEvaluationMapper;

import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ICritereEvaluationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CritereEvaluationService {

    private final ICritereEvaluationRepository critereRepo;
    private final CritereEvaluationMapper mapper;

    public CritereEvaluationResponseDTO create(CritereEvaluationRequestDTO dto) {
        CritereEvaluation entity = mapper.toEntity(dto);
        return mapper.toDTO(critereRepo.save(entity));
    }

    public List<CritereEvaluationResponseDTO> getBySprintId(String sprintId) {
        return critereRepo.findBySprintId(sprintId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<CritereEvaluationResponseDTO> getAll() {
        return critereRepo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CritereEvaluationResponseDTO> get(String id) {
        return critereRepo.findById(id)
                .map(mapper::toDTO);
    }

    public void deleteAll() {
        critereRepo.deleteAll();
    }

    public CritereEvaluationResponseDTO update(String id, CritereEvaluationRequestDTO dto) {
        CritereEvaluation entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(critereRepo.save(entity));
    }

    public void delete(String id) {
        critereRepo.deleteById(id);
    }
}

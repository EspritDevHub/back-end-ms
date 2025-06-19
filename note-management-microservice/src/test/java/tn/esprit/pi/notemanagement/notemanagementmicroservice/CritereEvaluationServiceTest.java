package tn.esprit.pi.notemanagement.notemanagementmicroservice;


import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ICritereEvaluationRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.Id;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CritereEvaluationServiceTest {

    @Mock
    private ICritereEvaluationRepository repo;

    @InjectMocks
    private CritereEvaluationService service;

    @Test
    void shouldReturnCriteriaBySprint() {
        CritereEvaluation crit = new CritereEvaluation("1", "N", "D", 1.0, "S1");
        when(repo.findBySprintId("S1")).thenReturn(List.of(crit));

        List<CritereEvaluation> result = service.getCriteriaBySprint("S1");
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        verify(repo).findBySprintId("S1");
    }
}

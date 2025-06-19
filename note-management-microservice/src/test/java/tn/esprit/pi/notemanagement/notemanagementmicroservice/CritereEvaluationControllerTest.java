package tn.esprit.pi.notemanagement.notemanagementmicroservice;

import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.CritereEvaluationMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers.CritereEvaluationController;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CritereEvaluationControllerTest {

    @Mock
    private CritereEvaluationService service;

    @InjectMocks
    private CritereEvaluationController ctrl;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ctrl).build();
    }

    @Test
    void shouldReturnCriteriaBySprint() throws Exception {
        var dto = new CritereEvaluationDTO("1","C","D",1.0,"S1");
        when(service.getCriteriaBySprint("S1"))
                .thenReturn(List.of(CritereEvaluationMapper.toEntity(dto)));

        mockMvc.perform(get("/api/criteres/sprints/sprint/S1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("C"));

        verify(service).getCriteriaBySprint("S1");
    }
}

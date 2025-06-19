package tn.esprit.pi.documentmanagement.documentmanagementmicroservice;


import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.DocumentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Enum.FileType;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository.IDocumentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.DocumentService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private IDocumentRepository docRepo;

    @InjectMocks
    private DocumentService service;


    @Test
    void shouldReturnDocumentsList_whenFound() {
        Document doc = Document.builder()
                .id("D2")
                .seanceId("S1")
                .etudiantId("E2")
                .build();
        when(docRepo.findBySeanceIdAndEtudiantId("S1", "E2"))
                .thenReturn(List.of(doc));

        List<DocumentDto> list = service.getDocumentsBySeance("S1", "E2");

        assertEquals(1, list.size());
        assertEquals("D2", list.get(0).getId());
        verify(docRepo).findBySeanceIdAndEtudiantId("S1", "E2");
    }

    @Test
    void shouldThrowException_whenDeleteNotFound() {
        when(docRepo.findByIdAndEtudiantId("X", "E3"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.deleteDocument("X", "E3")
        );
        assertTrue(ex.getMessage().contains("not found"));
        verify(docRepo).findByIdAndEtudiantId("X", "E3");
    }
}

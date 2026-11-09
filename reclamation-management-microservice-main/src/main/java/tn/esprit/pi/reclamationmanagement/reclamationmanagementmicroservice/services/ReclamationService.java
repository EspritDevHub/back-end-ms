package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.services;


import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.NotificationRequestDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.ReclamationRequestDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.ReclamationResponseDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Entities.Reclamation;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Enums.ReclamationStatus;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Mappers.ReclamationMapper;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.repository.IReclamationRepository;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.FeignClient.NotificationClient;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ReclamationService {
    private final IReclamationRepository reclamrepository;
    private final NotificationClient notificationClient;
    private final JiraService jiraService;
    private final EmailService emailService;

    public ReclamationResponseDTO createReclamation(String userId, ReclamationRequestDTO dto) {
        Reclamation reclamation = ReclamationMapper.toEntity(dto, userId);

        // Save to DB to get the ID (in case Jira needs it)
        reclamation = reclamrepository.save(reclamation);

        // Create Jira ticket
        String jiraKey = jiraService.createIssueFromReclamation(reclamation);
        if (jiraKey != null) {
            reclamation.setJiraTicketId(jiraKey);
            reclamrepository.save(reclamation); // Save Jira key
        }

        // Send notification
        NotificationRequestDTO notification = NotificationRequestDTO.builder()
                .title("Reclamation " + reclamation.getTitle() + " Created")
                .message("Your reclamation has been submitted successfully.")
                .userId(reclamation.getUserId())
                .read(false)
                .build();

        notificationClient.createNotification(notification);
        notificationClient.sendWebSocketNotification(notification);
        exportReclamationsToCSV();
        return ReclamationMapper.toDTO(reclamation);
    }

    public void deleteReclamation(String id, String userId) {
        NotificationRequestDTO notification = NotificationRequestDTO.builder()
                .title("Reclamation Deleted")
                .message("Your reclamation has been deleted successfully.")
                .userId(userId)
                .read(false)
                .build();
        notificationClient.createNotification(notification);
        notificationClient.sendWebSocketNotification(notification);
        reclamrepository.deleteById(id);
    }


    public ReclamationResponseDTO getById(String id) {

        return reclamrepository.findById(id)
                .map(ReclamationMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));
    }

    public List<ReclamationResponseDTO> getAll() {
        return reclamrepository.findAll()
                .stream()
                .map(ReclamationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReclamationResponseDTO update(String id, ReclamationRequestDTO dto) {
        Reclamation existing = reclamrepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setImage(dto.getImage());
        existing.setModifiedAt(LocalDateTime.now());

        boolean wasResolved = false;
        if (dto.getStatus() != null) {
            ReclamationStatus newStatus = ReclamationStatus.valueOf(dto.getStatus());
            if (newStatus == ReclamationStatus.RESOLVED && existing.getStatus() != ReclamationStatus.RESOLVED) {
                wasResolved = true;
            }
            existing.setStatus(newStatus);
        }

        Reclamation saved = reclamrepository.save(existing);

        // Notify the user
        NotificationRequestDTO notification = NotificationRequestDTO.builder()
                .title("Reclamation Updated")
                .message("Your reclamation has been updated successfully.")
                .userId(existing.getUserId())
                .read(false)
                .build();
        notificationClient.createNotification(notification);
        notificationClient.sendWebSocketNotification(notification);

        // 🔔 Send email if resolved
        if (wasResolved && existing.getEmail() != null) {
            emailService.sendReclamationResolvedEmail(existing.getEmail(), existing.getTitle() , null);
        }

        return ReclamationMapper.toDTO(saved);
    }
    public void saveJiraTicketId(String reclamationId, String jiraTicketId) {
        Optional<Reclamation> optional = reclamrepository.findById(reclamationId);
        if (optional.isPresent()) {
            Reclamation reclamation = optional.get();
            reclamation.setJiraTicketId(jiraTicketId);
            reclamation.setModifiedAt(LocalDateTime.now());
            reclamrepository.save(reclamation);
        } else {
            throw new RuntimeException("Reclamation not found with id: " + reclamationId);
        }
    }

    private void exportReclamationsToCSV() {
        List<Reclamation> reclamations = reclamrepository.findAll();
        try (FileWriter out = new FileWriter("reclamations_export.csv");
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(
                     "id", "title", "description", "status", "userId", "createdAt", "modifiedAt", "image", "jiraTicketId", "email"))) {

            for (Reclamation rec : reclamations) {
                printer.printRecord(
                        rec.getId(),
                        rec.getTitle(),
                        rec.getDescription(),
                        rec.getStatus(),
                        rec.getUserId(),
                        rec.getCreatedAt(),
                        rec.getModifiedAt(),
                        rec.getImage(),
                        rec.getJiraTicketId(),
                        rec.getEmail()
                );
            }

        } catch (IOException e) {
            e.printStackTrace(); // or log the error
        }
    }


}

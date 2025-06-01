package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JiraStatusSyncScheduler {
    private final JiraService jiraService;

    @Scheduled(fixedRate = 30  * 1000)
    public void syncJiraTicketsStatus() {
        jiraService.checkAndUpdateResolvedTickets();
    }
}

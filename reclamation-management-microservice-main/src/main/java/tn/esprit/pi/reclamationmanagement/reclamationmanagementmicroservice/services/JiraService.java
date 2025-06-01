package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Entities.Reclamation;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Enums.ReclamationStatus;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.repository.IReclamationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class JiraService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final IReclamationRepository reclamationRepository;

    @Value("${jira.base-url}")
    private String jiraApiUrl;

    @Value("${jira.project.key}")
    private String projectKey;

    @Value("${jira.email}")
    private String jiraEmail;

    @Value("${jira.api-token}")
    private String jiraToken;

    private String getAuthHeader() {
        String auth = jiraEmail + ":" + jiraToken;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public String createIssueFromReclamation(Reclamation reclamation) {
        String url = jiraApiUrl + "/rest/api/3/issue";

        Map<String, Object> description = Map.of(
                "type", "doc",
                "version", 1,
                "content", List.of(
                        Map.of(
                                "type", "paragraph",
                                "content", List.of(
                                        Map.of("type", "text", "text", reclamation.getDescription())
                                )
                        )
                )
        );

        Map<String, Object> fields = new HashMap<>();
        fields.put("project", Map.of("key", projectKey));
        fields.put("summary", reclamation.getTitle());
        fields.put("description", description);
        fields.put("issuetype", Map.of("name", "Bug"));

        Map<String, Object> payload = Map.of("fields", fields);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthHeader());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("key")) {
                return (String) responseBody.get("key");
            } else {
                log.error("Jira response missing issue key: {}", responseBody);
                throw new RuntimeException("Jira issue created but key not returned");
            }

        } catch (HttpClientErrorException e) {
            log.error("Jira API error: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Failed to create Jira issue");
        }
    }


    private String normalizeStatus(String status) {
        if (status == null) return "";
        String noAccents = Normalizer.normalize(status, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // Remove accents
        return noAccents.toLowerCase().replaceAll("[()]", "").trim(); // Remove parentheses and lowercase
    }

    public void checkAndUpdateResolvedTickets() {
        for (Reclamation rec : reclamationRepository.findAll()) {
            if (rec.getJiraTicketId() != null && rec.getStatus() != ReclamationStatus.RESOLVED) {
                String url = jiraApiUrl + "/rest/api/3/issue/" + rec.getJiraTicketId();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", getAuthHeader());

                HttpEntity<String> entity = new HttpEntity<>(headers);

                try {
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                    if (response.getStatusCode().is2xxSuccessful()) {
                        String body = response.getBody();

                        if (body != null) {
                            JSONObject json = new JSONObject(body);
                            String status = json.getJSONObject("fields")
                                    .getJSONObject("status")
                                    .getString("name");

                            // 💡 Debug log to see status
                            log.info("Jira ticket {} current status: {}", rec.getJiraTicketId(), status);

                            // ✅ Normalize and match status
                            String normalizedStatus = normalizeStatus(status); // e.g., "terminee"

                            if (normalizedStatus.equals("done")
                                    || normalizedStatus.equals("resolved")
                                    || normalizedStatus.equals("termine")
                                    || normalizedStatus.equals("terminee")
                                    || normalizedStatus.equals("closed")) {
                                rec.setStatus(ReclamationStatus.RESOLVED);
                                reclamationRepository.save(rec);
                                log.info("✅ Updated Reclamation {} to RESOLVED based on Jira status '{}'", rec.getId(), status);
                            } else {
                                log.info("ℹ️ Jira status '{}' not considered resolved for ticket {}", status, rec.getJiraTicketId());
                            }
                        }
                    }

                } catch (HttpClientErrorException e) {
                    log.error("❌ Error checking Jira issue {}: {}", rec.getJiraTicketId(), e.getResponseBodyAsString());
                }
            }
        }

    }
}

package com.example.groupmanagmenetms.service;

import com.example.groupmanagmenetms.DTO.UserResponseDTO;
import com.example.groupmanagmenetms.entity.Group;
import com.example.groupmanagmenetms.repository.IGroupRepositroy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@AllArgsConstructor
public class MailScheduler {

    private GroupService groupService;
    private final IGroupRepositroy repository;
    private JavaMailSender mailSender;
    private TemplateEngine templateEngine;
    @Scheduled(cron = "0 * * * * *")
    public void runEveryMinute() {
        groupService.resetAllGroupNotifications();
        var unNotifiedGroups = groupService.getUnnotifiedGroupMembers();
        for (Group group : unNotifiedGroups) {
            if (group.getMembers() != null && !group.getMembers().isEmpty() && !group.isMembersNotified()) {
                for (var member : group.getMembers()) {
                    try {
                        String htmlContent = generateGroupWelcomeEmail(member.getName(),group.getName(), group.getProjectName(), group.getMembers());
                        MimeMessage message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                        helper.setTo(member.getEmail());
                        helper.setSubject("You have been added to a PI group say hi!");
                        helper.setFrom("jobjob.bs2@gmail.com");
                        helper.setText(htmlContent, true);

                        mailSender.send(message);
                        System.out.println("Email sent to: " + member.getEmail());

                    } catch (MessagingException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Failed to send email", e);
                    }
                }
                group.setMembersNotified(true);
                repository.save(group);
            }
        }
    }

    private String generateGroupWelcomeEmail(String memberName, String groupName, String projectName, List<UserResponseDTO> members) {
        Context context = new Context();
        context.setVariable("memberName", memberName);
        context.setVariable("groupName", groupName);
        context.setVariable("projectName", projectName);
        context.setVariable("members", members);

        return templateEngine.process("welcome-to-group", context);
    }
}

package com.example.event.service;

import com.example.event.model.Announcement;
import com.example.event.repository.AnnouncementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl implements IAnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Override
    public Announcement add(Announcement announcement) {
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setUpdatedAt(LocalDateTime.now());
        if (announcement.getIsActive() == null) {
            announcement.setIsActive(true);
        }
        return announcementRepository.save(announcement);
    }

    @Override
    public Announcement update(Announcement announcement) {
        announcement.setUpdatedAt(LocalDateTime.now());
        return announcementRepository.save(announcement);
    }

    @Override
    public void delete(String id) {
        announcementRepository.deleteById(id);
    }

    @Override
    public List<Announcement> getAll() {
        return announcementRepository.findAll();
    }

    @Override
    public Optional<Announcement> getById(String id) {
        return announcementRepository.findById(id);
    }
    @Override
    public List<Announcement> getRecentActiveAnnouncements(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return announcementRepository.findAll().stream()
                .filter(a -> a.getCreatedAt().isAfter(since) && a.getIsActive())
                .toList();
    }

}

package com.example.event.service;

import com.example.event.model.Announcement;

import java.util.List;
import java.util.Optional;

public interface IAnnouncementService {
    Announcement add(Announcement announcement);
    Announcement update(Announcement announcement);
    void delete(String id);
    List<Announcement> getAll();
    Optional<Announcement> getById(String id);
    List<Announcement> getRecentActiveAnnouncements(int days);

    }

package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Version;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.IVersionRepository;

import java.util.List;

@Service
public class VersionService {

    @Autowired
    private IVersionRepository versionRepository;

    // Create or update version
    public Version saveVersion(Version version) {
        return versionRepository.save(version);
    }

    // Get all versions by documentId
    public List<Version> getVersionsByDocumentId(String documentId) {
        return versionRepository.findByDocumentId(documentId);
    }
}

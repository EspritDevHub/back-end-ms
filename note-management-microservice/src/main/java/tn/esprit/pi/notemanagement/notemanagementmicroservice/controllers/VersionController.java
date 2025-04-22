package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Version;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.VersionService;

import java.util.List;

@RestController
@RequestMapping("/api/versions")
public class VersionController {

    @Autowired
    private tn.esprit.pi.notemanagement.notemanagementmicroservice.services.VersionService versionService;

    // Create or update version
    @PostMapping
    public ResponseEntity<Version> createVersion(@RequestBody Version version) {
        Version savedVersion = versionService.saveVersion(version);
        return ResponseEntity.ok(savedVersion);
    }

    // Get all versions by documentId
    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<Version>> getVersionsByDocumentId(@PathVariable String documentId) {
        List<Version> versions = versionService.getVersionsByDocumentId(documentId);
        return ResponseEntity.ok(versions);
    }
}

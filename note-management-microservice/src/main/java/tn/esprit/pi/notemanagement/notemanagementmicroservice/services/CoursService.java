package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CoursDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Cours;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.CoursRepository;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    public List<Cours> getCoursByMatiereAndNiveau(String matiere, String niveau) {
        return coursRepository.findByMatiereContainingIgnoreCaseAndNiveau(matiere, niveau);
    }
}


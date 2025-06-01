package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;

import java.util.List;

public class ClassementResponse {
    public List<SeanceClassement> classement;
    public int note_seance_inserted;

    public static class SeanceClassement {
        public String seanceId;
        public String seanceTitre;
        public List<EtudiantNote> topEtudiants;
    }

    public static class EtudiantNote {
        public String etudiantId;
        public double valeur;
        public String badge;
    }
}
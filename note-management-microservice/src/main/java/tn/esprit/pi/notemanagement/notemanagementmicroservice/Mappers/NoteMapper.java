package tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers;


import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.NoteDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;

public class NoteMapper {

    public static NoteDTO toDto(Note note) {
        if (note == null) return null;

        return NoteDTO.builder()
                .id(note.getId())
                .seanceId(note.getSeanceId())
                .sprintId(note.getSprintId())
                .critereId(note.getCritereId())
                .etudiantId(note.getEtudiantId())
                .groupeId(note.getGroupeId())
                .valeur(note.getValeur())
                .typeNote(note.getTypeNote())
                .build();
    }

    public static Note toEntity(NoteDTO dto) {
        if (dto == null) return null;

        return Note.builder()
                .id(dto.getId())
                .seanceId(dto.getSeanceId())
                .sprintId(dto.getSprintId())
                .critereId(dto.getCritereId())
                .etudiantId(dto.getEtudiantId())
                .groupeId(dto.getGroupeId())
                .valeur(dto.getValeur())
                .build();
    }
}
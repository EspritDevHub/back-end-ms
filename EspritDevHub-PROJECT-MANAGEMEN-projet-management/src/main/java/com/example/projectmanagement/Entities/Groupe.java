package com.example.projectmanagement.Entities;




import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "groupes")
public class Groupe {

    @Id
    private String id;

    private String nom;

    @DBRef
    private List<User> membres;

    // Constructeurs
    public Groupe() {}

    public Groupe(String nom, List<User> membres) {
        this.nom = nom;
        this.membres = membres;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<User> getMembres() {
        return membres;
    }

    public void setMembres(List<User> membres) {
        this.membres = membres;
    }
}

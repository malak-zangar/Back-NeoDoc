package com.example.backneodoc.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departement")
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE},
            mappedBy = "departements")
    private Set<Document> document_d = new HashSet<>();

    public Departement() {
    }

    public Departement(String nom, Set<Document> document_d) {
        this.nom = nom;
        this.document_d = document_d;
    }

    public Departement(String nom) {
        this.nom = nom;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNom() {return nom;}

    public void setNom(String nom) {this.nom = nom;}

    public Set<Document> getDocument_d() {return document_d;}

    public void setDocument_d(Set<Document> document_d) {this.document_d = document_d;}

}

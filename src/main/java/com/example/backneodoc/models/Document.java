package com.example.backneodoc.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "URL")
    private String url;

    @Column(name = "type", nullable = false)
    private String type;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    //@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE},mappedBy ="doc_favoris" )
    //private Set<User> favoris = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "doc_tag", joinColumns = { @JoinColumn(name = "post_id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "doc_dep", joinColumns = { @JoinColumn(name = "id_doc") }, inverseJoinColumns = { @JoinColumn(name = "id_dep") })
    private Set<Departement> departements = new HashSet<>();

    public Document() {
    }

    public Document(String titre,String url , String type , byte[] data) {
        this.titre = titre;
        this.url=url;
        this.type=type;
        this.data=data;
    }

    public Document(String titre, String url, String type, Set<Tag> tags,Set<Departement> dep) {
        this.titre = titre;
        this.url = url;
        this.type = type;
        this.tags = tags;
        this.departements=dep;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public String getTitre() {return titre;}

    public void setTitre(String titre) {this.titre = titre;}


    public Set<Tag> getTags() {return tags;}

    public void setTags(Set<Tag> tags) {this.tags = tags;}

    public byte[] getData() {return data;}

    public void setData(byte[] data) {this.data = data;}

    public Set<Departement> getDepartements() {return departements;}

    public void setDepartements(Set<Departement> departements) {this.departements = departements;}
/*
    public Set<User> getFavoris() {
        return favoris;
    }

    public void setFavoris(Set<User> favoris) {
        this.favoris = favoris;
    }*/
}
package com.example.backneodoc.payload.response;

import com.example.backneodoc.models.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean enabled;
    private String poste;
    private List<String> roles;
    private Set<Document> doc_favoris = new HashSet<>();


    public JwtResponse(String accessToken, Long id, String username, String email, Boolean enabled, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled=enabled;
        this.roles = roles;
    }
    public JwtResponse(String accessToken, Long id, String username,String firstname,String lastname, String email, Boolean enabled, String poste,Set<Document> doc_favoris, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email = email;
        this.enabled=enabled;
        this.poste=poste;
        this.doc_favoris=doc_favoris;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getFirstname() {return firstname;}

    public void setFirstname(String firstname) {this.firstname = firstname;}

    public String getLastname() {return lastname;}

    public void setLastname(String lastname) {this.lastname = lastname;}

    public Boolean getEnabled() {return enabled;}

    public void setEnabled(Boolean enabled) {this.enabled = enabled;}

    public String getPoste() {return poste;}

    public void setPoste(String poste) {this.poste = poste;}

    public Set<Document> getDoc_favoris() {
        return doc_favoris;
    }

    public void setDoc_favoris(Set<Document> doc_favoris) {
        this.doc_favoris = doc_favoris;
    }
}

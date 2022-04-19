package com.example.backneodoc.security.services;

import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.Tag;
import com.example.backneodoc.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    @JsonIgnore
    private String password;

    private Boolean enabled;

    private String poste;

    private Set<Document> doc_favoris = new HashSet<>();

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password,Boolean enabled,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled=enabled;
        this.authorities = authorities;
    }
    public UserDetailsImpl(Long id, String username,String firstname,String lastname, String email, String password,Boolean enabled,String poste,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email = email;
        this.password = password;
        this.enabled=enabled;
        this.poste=poste;
        this.authorities = authorities;

    }

    public UserDetailsImpl(Long id, String username,String firstname,String lastname, String email, String password,Boolean enabled,String poste,
                           Set<Document> doc_favoris,Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email = email;
        this.password = password;
        this.enabled=enabled;
        this.poste=poste;
        this.doc_favoris=doc_favoris;
        this.authorities = authorities;

    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                user.getPoste(),
                user.getDoc_favoris(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Boolean getEnabled(){return enabled;}

    public String getPoste(){return poste;}

    public Set<Document> getDoc_favoris(){ return doc_favoris;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }


}

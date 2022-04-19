package com.example.backneodoc.repository;

import com.example.backneodoc.models.Role;
import com.example.backneodoc.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByLibelle(String libelle);
}

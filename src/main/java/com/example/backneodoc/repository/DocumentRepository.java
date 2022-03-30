package com.example.backneodoc.repository;

import com.example.backneodoc.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {

    Optional<Document> findByTitre(String name);
}

package com.example.backneodoc.repository;

import com.example.backneodoc.models.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByTitre(String name);
    List<Document> findAll(Sort by);


    List<Document> findAllByTitre(String name);

    Optional<Document> findById(Long id);

    Boolean existsByTitre(String titre);

    Document findByDepartements(String dep);

}

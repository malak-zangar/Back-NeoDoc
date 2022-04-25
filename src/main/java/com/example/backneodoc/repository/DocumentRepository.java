package com.example.backneodoc.repository;

import com.example.backneodoc.models.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByTitre(String name);
    List<Document> findAll(Sort by);

    List<Document> getDocumentsByType(String type);
    List<Document> getDocumentsByTitre(String titre);
    List<Document> getDocumentsByTags(String tag);

    List<Document> findAllByTitre(String name);
    List<Document>  findByTitreContaining(String name);
    Optional<Document> findById(Long id);

    Boolean existsByTitre(String titre);

    Document findByDepartements(String dep);
    List<Document> searchDocumentByTitreIsContaining(String titre);
    List<Document> searchDocumentByDepartementsIsContaining(String dep);
    List<Document> searchDocumentByTypeIsContaining(String dep);
    List<Document> searchDocumentByTagsIsContaining(String dep);

}

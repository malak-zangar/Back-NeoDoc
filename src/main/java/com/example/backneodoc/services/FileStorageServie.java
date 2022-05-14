package com.example.backneodoc.services;

import com.example.backneodoc.models.Document;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface FileStorageServie {
    public void init();

    public void save(MultipartFile file);

    //public void saveMultiple(List<Document> uploadedFiles, Set<String> tags, String dep);

    public List<Document> getAllFiles();

    public ResponseEntity<?> saveMultiple(MultipartFile[] uploadedFile, Set<String> tags, String dep);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

}

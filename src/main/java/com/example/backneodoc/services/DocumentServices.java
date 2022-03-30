package com.example.backneodoc.services;

import com.example.backneodoc.Exceptions.ResourceNotFoundException;
import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.ERole;
import com.example.backneodoc.models.Role;
import com.example.backneodoc.models.User;
import com.example.backneodoc.payload.request.DocRequest;
import com.example.backneodoc.payload.request.SignupRequest;
import com.example.backneodoc.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.IOException;
import java.util.*;

@Service
public class DocumentServices {

    @Autowired
    DocumentRepository documentRepository;

    public Document store(MultipartFile file) throws IOException {
        String filename=file.getOriginalFilename();
        Document document=new Document(filename,null,file.getContentType(),file.getBytes());
        return documentRepository.save(document);
    }

    public Document saveFile(MultipartFile file) {
        String docname = file.getOriginalFilename();
        try {
            Document doc = new Document(docname,null,file.getContentType(),file.getBytes());
            return documentRepository.save(doc);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document getDocById(Long id){
        Optional<Document> fileOptional=documentRepository.findById(id);
        if(fileOptional.isPresent()){return fileOptional.get();}
        return null;}

    public Document getDocByTitle(String name){
        Optional<Document> fileOptional=documentRepository.findByTitre(name);
        if(fileOptional.isPresent()){return fileOptional.get();}
        return null;}

    public List<Document> getAllDoc(){
        return documentRepository.findAll();
    }

    public Map<String, Boolean> deleteDoc(@PathVariable(value = "id") Long docId)
            throws ResourceNotFoundException {
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé pour cet id:: " + docId));
        documentRepository.delete(document);
        Map<String, Boolean> response = new HashMap<>();
        response.put("supprimé", Boolean.TRUE);
        return response;
    }

    public ResponseEntity<Document> updateDoc(@PathVariable(value = "id") Long docId,
                                           @RequestBody DocRequest docRequest) throws ResourceNotFoundException {
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("document non trouvé pour cet id: " + docId));

        document.setTitre(docRequest.getTitre());
        document.setTags(docRequest.getTag());
        document.setDepartements(docRequest.getDep());

        final Document updatedDoc = documentRepository.save(document);
        return ResponseEntity.ok(updatedDoc);
    }


}

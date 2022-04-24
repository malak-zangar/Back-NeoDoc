package com.example.backneodoc.services;

import com.example.backneodoc.Exceptions.ResourceNotFoundException;
import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.Tag;
import com.example.backneodoc.payload.request.DocRequest;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.DocumentRepository;
import com.example.backneodoc.repository.TagRepository;
import com.example.backneodoc.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class DocumentServices {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public Document store(MultipartFile file) throws IOException {
        String filename=file.getOriginalFilename();
        Document document=new Document(filename,file.getContentType(),file.getBytes());
        return documentRepository.save(document);
    }

      //  public Document saveFile(MultipartFile file, Set<Tag> tags, Set<Departement> dep) {

    public Document saveFile(MultipartFile file, Set<String> tags, String dep) {
            Set<Tag> stag= new HashSet<>();
            String docname = file.getOriginalFilename();
            Document doc=new Document();

            try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            for ( String tag : tags) {
                System.out.println("hello");
                if (tagRepository.findByLibelle(tag) != null) {
                    System.out.println("le tag existe");
                    stag.add(tagRepository.findByLibelle(tag));
                } else {
                    Tag ntag = new Tag(tag);
                    tagRepository.save(ntag);
                    stag.add(ntag);
                }
            }
            doc = new Document(docname, file.getContentType(), file.getBytes(),stag,dep);
            Document doc1=documentRepository.save(doc);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("malak.zangar@neoxam.com");
            mailMessage.setSubject("Ajout d'un nouveau document");
            mailMessage.setFrom("malak.zangar@etudiant-isi.utm.tn");
            mailMessage.setText("L'employé ' " + userDetails.getUsername() +" ' a ajouté un nouveau document intitulé ' " + docname + " ' dans le département " +dep);
            javaMailSender.send(mailMessage);

            return doc1;

        } catch (Exception e) {
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
        if(fileOptional.isPresent()){ System.out.println("here");
            return fileOptional.get();}
        return null;}

    public List<Document> getAllDoc(){
        return documentRepository.findAll(Sort.by(Sort.Direction.ASC, "type","titre"));
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

    public ResponseEntity<Document> updateDoc(Long docId,String titre,String dep,Set<String> tags) throws ResourceNotFoundException {
        Set<Tag> stag= new HashSet<>();
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("document non trouvé pour cet id: " + docId));
        List<Document> t=documentRepository.findAllByTitre(document.getTitre());
        if(t!=null)

        {for(Document doc:t){
            if (doc.getDepartements().equals(dep) && (doc.getId()!=document.getId()))
            {
                System.out.println("Nom du fichier " + titre + " existe déja dans le departement " + dep);
                 return ResponseEntity.badRequest().body(null);
            }
        }}
        for ( String tag : tags){
            System.out.println("hello");
            if (tagRepository.findByLibelle(tag)!=null){
                System.out.println("le tag existe");
                stag.add(tagRepository.findByLibelle(tag));
            }
            else {
                Tag  ntag = new Tag(tag);
                tagRepository.save(ntag);
                stag.add(ntag);}
        }

        document.setTags(stag);
        document.setTitre(titre);
        document.setDepartements(dep);

        final Document updatedDoc = documentRepository.save(document);
        return ResponseEntity.ok(updatedDoc);
    }


}


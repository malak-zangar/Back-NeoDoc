package com.example.backneodoc.Controllers;

import com.example.backneodoc.Exceptions.ResourceNotFoundException;
import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.Tag;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.DocumentRepository;
import com.example.backneodoc.repository.TagRepository;
import com.example.backneodoc.services.DocumentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins="*",maxAge = 3600)

@RestController
@RequestMapping("document")
public class FileUploadController {

    @Autowired
    public DocumentServices documentServices;

    @Autowired
    public DocumentRepository documentRepository;

    @Autowired
    public TagRepository tagRepository;

    @Autowired
    JavaMailSender javaMailSender;

    /*@PostMapping("/upload")
    public Document uploadFile(@RequestParam("document") MultipartFile file) throws IOException {

       return documentServices.store(file);
    }*/

    @PostMapping("/upload")
       // public  ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("tags") Set<Tag> tags, @RequestParam("dep") Set<Departement> dep) {
     public  ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("tags") Set<String> tags, @RequestParam("dep") String dep) {

        for (MultipartFile file : files) {
            //  if (documentRepository.existsByTitre(file.getOriginalFilename())
            List<Document> t=documentRepository.findAllByTitre(file.getOriginalFilename());
                if(t!=null)
                 {for(Document doc:t){
                    if (doc.getDepartements().equals(dep))
                    //   && documentRepository.findByDepartements(dep) != null)
                    {
                    System.out.println("Nom du fichier " + file.getOriginalFilename() + " existe déja " +
                            "dans le departement " + dep);
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Nom du fichier " + file.getOriginalFilename() + " existe déja" +
                                    "dans le departement " + dep));
                }
            }}

            if (documentServices.saveFile(file, tags, dep) != null) {
                return ResponseEntity.ok(new MessageResponse("fichier(s) ajouté(s) avec succée!"));

            } else {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Une ereure est servenue"));
            }
        }
        return ResponseEntity.ok(new MessageResponse(""));
    }

    @GetMapping("/{id}")
    public Document getFile(@PathVariable Long id) throws IOException {
        return documentServices.getDocById(id);
    }

    @GetMapping("/name/{name}")
    public Document getFile(@PathVariable String name) throws IOException {
        return documentServices.getDocByTitle(name);
    }

    @GetMapping("/list")
    public List<Document> getDocumentList() {
        return documentServices.getAllDoc();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        return documentServices.deleteDoc(userId);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    /*public ResponseEntity<Document> updateDoc(@PathVariable(value = "id") Long docId,
                                              @RequestBody DocRequest docRequest,
                                              @RequestParam(value="tags") Set<String> tags) throws ResourceNotFoundException {
        return documentServices.updateDoc(docId,docRequest,tags);}*/
    public ResponseEntity<Document> updateDoc(@PathVariable(value = "id") Long docId,
                    @RequestParam(value="titre") String titre,@RequestParam(value="dep") String dep,
                                              @RequestParam(value="tags") Set<String> tags) throws ResourceNotFoundException {
        return documentServices.updateDoc(docId,titre,dep,tags);}

    @GetMapping("/download/{id:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
        // Load file as Resource
        Document document = documentServices.getDocById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitre() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getTags(){
        return new ResponseEntity<>(tagRepository.findAll(),HttpStatus.OK);
    }

    }
    



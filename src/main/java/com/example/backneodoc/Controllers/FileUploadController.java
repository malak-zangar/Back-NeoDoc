package com.example.backneodoc.Controllers;

import com.example.backneodoc.Exceptions.ResourceNotFoundException;
import com.example.backneodoc.models.Document;
import com.example.backneodoc.payload.request.DocRequest;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.DocumentRepository;
import com.example.backneodoc.services.DocumentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;

@CrossOrigin(origins="*",maxAge = 3600)
/*
@RestController
public class FileUploadController {
    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;

    @PostMapping("/uploadFile")
    public ResponseEntity<Object> fileUpload(@RequestParam("File")MultipartFile file) throws IOException {
        File myFile = new File(FILE_DIRECTORY+file.getOriginalFilename());
        myFile.createNewFile();
        FileOutputStream fos=new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        return new ResponseEntity<Object>("The file uploaded successfully", HttpStatus.OK);
    }} */

@RestController
@RequestMapping("document")
public class FileUploadController {

    @Autowired
    public DocumentServices documentServices;

    @Autowired
    public DocumentRepository documentRepository;

    @Autowired
    JavaMailSender javaMailSender;

    /*@PostMapping("/upload")
    public Document uploadFile(@RequestParam("document") MultipartFile file) throws IOException {

       return documentServices.store(file);
    }*/

    @PostMapping("/upload")
   // public  ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,@RequestParam("username") String username) {
        public  ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
            for (MultipartFile file: files) {
            documentServices.saveFile(file);}

        return ResponseEntity.ok(new MessageResponse("fichier ajouté avec succée!"));
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
    public ResponseEntity<Document> updateDoc(@PathVariable(value = "id") Long docId,
                                              @RequestBody DocRequest docRequest) throws ResourceNotFoundException {
        return documentServices.updateDoc(docId,docRequest);}

    @GetMapping("/download/{id:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
        // Load file as Resource
        Document document = documentServices.getDocById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitre() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }



    }
    



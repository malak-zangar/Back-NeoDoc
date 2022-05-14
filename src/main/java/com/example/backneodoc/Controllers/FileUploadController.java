package com.example.backneodoc.Controllers;

import com.example.backneodoc.Exceptions.ResourceNotFoundException;
import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.Tag;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.DocumentRepository;
import com.example.backneodoc.repository.TagRepository;
import com.example.backneodoc.security.services.UserDetailsImpl;
import com.example.backneodoc.services.DocumentServices;
import com.example.backneodoc.services.VideoStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.example.backneodoc.constants.ApplicationConstants.VIDEO;

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

    @Autowired
    VideoStreamService videoStreamService;

    @PostMapping(path = "/upload")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files")  MultipartFile[] uploadedFiles, @RequestParam("tags") Set<String> tags, @RequestParam("dep")  String dep) {
       System.out.println("hello");
        documentServices.saveMultiple(uploadedFiles,tags, dep);
        return ResponseEntity.ok(new MessageResponse("success"));
    }

        @GetMapping("/list")
    public ResponseEntity<List<Document>> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(documentServices.getAllFiles());
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        return documentServices.deleteDoc(userId);
    }

    @GetMapping("/name/{name:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = documentServices.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename+  "\"").body(file);
    }
   

    @GetMapping("/files/stream/{fileName}")
    public ResponseEntity<byte[]> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                              @PathVariable("fileName") String fileName) {
        return videoStreamService.prepareContent(fileName, httpRangeList);
    }


    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getTags(){
        return new ResponseEntity<>(tagRepository.findAll(),HttpStatus.OK);
    }

    @GetMapping("/download/{id:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        // Load file as Resource
        Document document = documentServices.getDocById(id);
        Path path = Paths.get(document.getPath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentLength(document.getSize())
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(resource);
    }

    @GetMapping("/recherche/type/{type}")
    public List<Document> getDocumentByType( @PathVariable(value="type") String type) {
        return documentServices.getSearchDocType(type);
    }
    @GetMapping("/recherche/titre/{titre}")
    public List<Document> getDocumentByTitre( @PathVariable(value="titre") String titre) {
        return documentServices.getSearchDocTitre(titre);
    }
    @GetMapping("/recherche/tag/{tag}")
    public List<Document> getDocumentByTag(@PathVariable(value="tag") String tag) {
        return documentServices.getSearchDocTag(tag);
    }
    @GetMapping("/recherche/dep/{dep}")
    public List<Document> getDocumentByDep(@PathVariable(value="dep") String dep) {
        return documentServices.getSearchDocDep(dep);
    }
    @GetMapping("/getAll/inDep/{dep}")
    public List<Document> getAllByDep(@PathVariable(value="dep") String dep) {
        return documentServices.getbyDep(dep);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Document> updateDoc(@PathVariable(value = "id") Long docId,
                                              @RequestParam(value="titre") String titre,@RequestParam(value="dep") String dep,
                                              @RequestParam(value="tags") Set<String> tags) throws ResourceNotFoundException {
        return documentServices.updateDoc(docId,titre,dep,tags);}


    @GetMapping("/{id}")
    public Document getFilebyid(@PathVariable Long id) throws IOException {
        return documentServices.getDocById(id);
    }
}
    



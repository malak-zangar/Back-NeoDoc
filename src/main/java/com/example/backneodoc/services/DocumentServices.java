package com.example.backneodoc.services;

import com.example.backneodoc.Controllers.FileUploadController;
import com.example.backneodoc.Exceptions.ResourceNotFoundException;
import com.example.backneodoc.models.*;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.DocumentRepository;
import com.example.backneodoc.repository.RoleRepository;
import com.example.backneodoc.repository.TagRepository;
import com.example.backneodoc.repository.UserRepository;
import com.example.backneodoc.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.backneodoc.constants.ApplicationConstants.WAMP;

@Service
public class DocumentServices implements FileStorageServie {
    public DocumentServices(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    RoleRepository roleRepository;

    private final Path root = Paths.get("uploads/");
private final Path WAMP = Paths.get("C:/wamp64/www/uploadsNeo/");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?>  saveMultiple(MultipartFile[] uploadedFile, Set<String> tags, String dep) {
System.out.println("heelo1");
        for(MultipartFile fileDto: uploadedFile){
            System.out.println("heelo2");

            List<Document> t=documentRepository.findAllByName(fileDto.getOriginalFilename());
            if(t!=null)
            {for(Document doc:t){
                if (doc.getDepartements().equals(dep))
                {
                    System.out.println("Nom du fichier " + fileDto.getOriginalFilename() + " existe déja " + "dans le departement " + dep);
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Nom du fichier " + fileDto.getOriginalFilename() + " existe déja" +
                                    "dans le departement " + dep));
                }}}
            String path="C:/Users/Admin/Desktop/Back-NeoDoc2/uploads/"+fileDto.getOriginalFilename();
            // String path = "/home/kamel/Work/pfe/pfe/projectttt/plateforme2/uploads";
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String owner=userDetails.getUsername();
            String docname = fileDto.getOriginalFilename();

            Set<Tag> stag=new HashSet<>();
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
            try {
                Files.copy(fileDto.getInputStream(), this.root.resolve(fileDto.getOriginalFilename()));
                Files.copy(fileDto.getInputStream(), this.WAMP.resolve(fileDto.getOriginalFilename()));
            } catch (Exception e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
            Document file = new Document(fileDto.getOriginalFilename(),fileDto.getSize(),fileDto.getContentType(), owner,path,stag,dep);

            documentRepository.save(file);
            Role admin = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Erreur: Role n'existe pas."));
            List<String> mails=userRepository.searchUserByRole(admin.getId().longValue());
            for (String mail:mails){
                System.out.println(mail);
            }
            for (String mail:mails){
                System.out.println(mail);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(mail);
                mailMessage.setSubject("Ajout d'un nouveau document");
                mailMessage.setFrom("malak.zangar@etudiant-isi.utm.tn");
                mailMessage.setText("L'employé ' " + owner +" ' a ajouté un nouveau document intitulé ' " + docname + " ' dans le département " +dep);
                javaMailSender.send(mailMessage);
            }
        }
        return ResponseEntity.ok(new MessageResponse(""));

    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public List<Document> getAllFiles() {
        List<Document> fileList= documentRepository.findAll(Sort.by(Sort.Direction.ASC,"contentType","name"));
        System.out.println("t7ato f liste");
        List<Document> fileInfos = fileList.stream().map(file -> {
            System.out.println("t7ato fl iste2  "+ file.getId());
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "getFile",file.getName()).build().toString();
file.setPath(url);
            return new Document(file);
        }).collect(Collectors.toList());
        return fileInfos;
    }

    public Map<String, Boolean> deleteDoc(Long docId) throws ResourceNotFoundException {
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé pour cet id:: " + docId));
        List<User> users=userRepository.searchUserByFav(document.getId());
        for(User user:users){
            Set<Document> fav = user.getDoc_favoris();
            fav.remove(document);
            user.setDoc_favoris(fav);
        }
        Path path= Paths.get(document.getPath()) ;
        try {
            // Delete file or directory
            Files.deleteIfExists(path);
            System.out.println("File or directory deleted successfully");
        } catch (NoSuchFileException ex) {
            System.out.printf("No such file or directory: %s\n", path);
        } catch (DirectoryNotEmptyException ex) {
            System.out.printf("Directory %s is not empty\n", path);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        documentRepository.delete(document);
        Map<String, Boolean> response = new HashMap<>();
        response.put("supprimé", Boolean.TRUE);
        return response;
    }

    public Document getDocByName(String name){
        Optional<Document> fileOptional=documentRepository.findByName(name);
        if(fileOptional.isPresent()){ System.out.println("here");
            return fileOptional.get();}
        return null;}

    public Document getDocById(Long id){
        Optional<Document> fileOptional=documentRepository.findById(id);
        if(fileOptional.isPresent()){ System.out.println("here");
            return fileOptional.get();}
        return null;}

    public List<Document> getSearchDocTitre(String titre){
        //   return documentRepository.searchDocumentByTitreContaining(titre);
        return documentRepository.findByNameContaining(titre);
    }
    public List<Document> getSearchDocDep(String dep){
        return documentRepository.findByDepartementsContaining(dep);
    }
    public List<Document> getSearchDocType(String type){
        return documentRepository.findByContentTypeContaining(type);
    }
    public List<Document> getSearchDocTag(String tag){
        return documentRepository.searchDocumentByTags(tag);
    }
    public List<Document> getbyDep(String dep){
        return documentRepository.findByDepartements(dep);
    }

    public ResponseEntity<Document> updateDoc(Long docId,String titre,String dep,Set<String> tags) throws ResourceNotFoundException {
        Set<Tag> stag= new HashSet<>();
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new ResourceNotFoundException("document non trouvé pour cet id: " + docId));
        List<Document> t=documentRepository.findAllByName(document.getName());
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
        System.out.println(titre);
        document.setName(titre);
        document.setDepartements(dep);

        final Document updatedDoc = documentRepository.save(document);
        return ResponseEntity.ok(updatedDoc);
    }
}


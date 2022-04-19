package com.example.backneodoc.Controllers;

import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.User;
import com.example.backneodoc.payload.request.SignupRequest;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.DocumentRepository;
import com.example.backneodoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DocumentRepository docRepository;

    @PutMapping("/doc/{userId}/{docId}")
    public ResponseEntity<?> addToFav(@PathVariable(value = "userId") Long userId , @PathVariable(value = "docId") Long docId) {
        System.out.println("hiiii put");
        User user = userRepository.findById(userId).orElse(null);
        Set<Document> Fav =user.getDoc_favoris();
        Document document=docRepository.findById(docId).orElse(null);
        Fav.add(document);
        user.setDoc_favoris(Fav);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("fav ajouté avec succée!"));
    }

    @PutMapping("/doc/{userId}")
    public ResponseEntity<?> addToFavo(@PathVariable(value = "userId") Long userId , @RequestBody Set<Document> fav) {
        System.out.println("hiiii put");
        User user = userRepository.findById(userId).orElse(null);
        Set<Document> Fav =user.getDoc_favoris();
        Fav=fav;
        user.setDoc_favoris(Fav);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("fav ajouté avec succée!"));
    }

    @DeleteMapping("/doc/{userId}/{docId}")
    public ResponseEntity<?> removeFromFav(@PathVariable(value = "userId") Long userId , @PathVariable(value = "docId") Long docId) {
        User user = userRepository.findById(userId).orElse(null);
        Set<Document> Fav = user.getDoc_favoris();
        Document document=docRepository.findById(docId).orElse(null);
        Fav.remove(document);
        user.setDoc_favoris(Fav);

        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("fav supprimé avec succée!"));
    }

    @GetMapping("/doc/{userId}")
    public Set<Document> getFav(@PathVariable(value = "userId") Long userId ) {
        User user = userRepository.findById(userId).orElse(null);
        Set<Document> Fav = user.getDoc_favoris();
        return Fav;
    }

    @PostMapping("/doc/update")
    public ResponseEntity<?> toFav(@PathVariable(value="userId") Long userId,@Valid @RequestBody Set<Document> Fav) {
        User a = userRepository.findById(userId).orElse(null);
       /* a.setDoc_favoris().stream().forEach(sp -> System.out.println(sp.getTitre()));
        Set<Document> files = new HashSet<>();
        for (Document f : user.setDoc_favoris(this.f)) {
            System.out.println(f.getId());
            Optional<Document> ff = docRepository.findById(f.getId());
            Document f3 = ff.get();
            System.out.println(ff);
            files.add(f3);
        }*/

        a.setDoc_favoris(Fav);
        userRepository.save(a);
        return new ResponseEntity<>(new MessageResponse("User updated successfully!"), HttpStatus.OK);
    }


}

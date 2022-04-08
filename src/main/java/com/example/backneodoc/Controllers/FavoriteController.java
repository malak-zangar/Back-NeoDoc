package com.example.backneodoc.Controllers;

import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.User;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.DocumentRepository;
import com.example.backneodoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
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

    @DeleteMapping("/doc/{userId}/{docId}")
    public ResponseEntity<?> removeFromFav(@PathVariable(value = "userId") Long userId , @PathVariable(value = "docId") Long docId) {
       System.out.println("hiiii");
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


}

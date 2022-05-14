package com.example.backneodoc.Controllers;

import com.example.backneodoc.Exceptions.ResourceNotFoundException;
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

    @PutMapping("/addtofav/{idu}/{idd}")
    public ResponseEntity<User> addtofavo(@PathVariable(value = "idu") Long idu,@PathVariable(value = "idd") Long idd, @RequestParam(value="doc") Long docid
    ) throws ResourceNotFoundException {
        Document document = docRepository.findById(docid)
                .orElseThrow(() -> new ResourceNotFoundException("document non trouvé pour cet id: " + docid));
        User user = userRepository.findById(idu)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet id: " + idu));
        Set<Document> nfav=user.getDoc_favoris();
        nfav.add(document);
        user.setDoc_favoris(nfav);

        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
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


}

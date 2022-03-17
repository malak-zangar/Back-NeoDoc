package com.example.backneodoc.Controllers;

import com.example.backneodoc.Exceptions.ResourceNotFoundException;
import com.example.backneodoc.models.ERole;
import com.example.backneodoc.models.Role;
import com.example.backneodoc.models.User;
import com.example.backneodoc.payload.request.SignupRequest;
import com.example.backneodoc.payload.response.MessageResponse;
import com.example.backneodoc.repository.RoleRepository;
import com.example.backneodoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Valid;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gestion")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour l'id :: " + userId));
        return ResponseEntity.ok().body(user);
    }

   /* @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }*/
   @PostMapping("/users")
   public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signUpRequest) {
      if (userRepository.existsByUsername(signUpRequest.getUsername())) {
           return ResponseEntity
                   .badRequest()
                   .body(new MessageResponse(" Nom utilisateur existe déja!"));}

       if (userRepository.existsByEmail(signUpRequest.getEmail())) {
           return ResponseEntity
                   .badRequest()
                   .body(new MessageResponse(" Email existe déja!")); }

       // Create new user's account
       User user = new User(
               signUpRequest.getFirstname(),
               signUpRequest.getLastname(),
               signUpRequest.getUsername(),
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()),
               signUpRequest.getPoste() );

       Set<String> strRoles = signUpRequest.getRole();
       Set<Role> roles = new HashSet<>();

       if (strRoles == null) {
           Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                   .orElseThrow(() -> new RuntimeException("Erreur: Role n'existe pas."));
           roles.add(adminRole);
       } else {
           strRoles.forEach(role -> {
               switch (role) {
                   case "user":
                       Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                               .orElseThrow(() -> new RuntimeException("Erreur: Role n'existe pas."));
                       roles.add(userRole);
                       break;

                   default:
                       Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                               .orElseThrow(() -> new RuntimeException("Erreur: Role n'existe pas."));
                       roles.add(adminRole);
               }
           });
       }

       user.setRoles(roles);
       user.setEnabled(true);
       userRepository.save(user);

       return ResponseEntity.ok(new MessageResponse("utilisateur enregistré avec succée!"));
   }


    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                                   @Valid @RequestBody SignupRequest signupRequest) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet id:: " + userId));

        user.setEmail(signupRequest.getEmail());
        user.setLastname(signupRequest.getLastname());
        user.setFirstname(signupRequest.getFirstname());
        user.setUsername(signupRequest.getUsername());
        user.setPoste(signupRequest.getPoste());
        user.setEnabled(signupRequest.getEnabled());

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Erreur: Role n'existe pas."));
            roles.add(adminRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "user":
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Erreur: Role n'existe pas."));
                        roles.add(userRole);
                        break;

                    default:
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Erreur: Role n'existe pas."));
                        roles.add(adminRole);
                }
            });
        }

        user.setRoles(roles);

      //  user.setRoles(userDetails.getRoles());

        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé pour cet id:: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("supprimé", Boolean.TRUE);
        return response;
    }
}

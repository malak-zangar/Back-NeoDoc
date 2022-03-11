package com.example.backneodoc.security.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.example.backneodoc.models.User;
import com.example.backneodoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MDPUserService {
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 200;

    @Autowired
    private UserRepository userRepository;

    public String forgotPassword(String email) {

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (!userOptional.isPresent()) {return "email non trouvé.";}

        User user = userOptional.get();
        user.setToken(generateToken());
        user.setTokenCreationDate(LocalDateTime.now());

        user = userRepository.save(user);

        return user.getToken();
    }

    public String resetPassword(String token, String password) {

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByToken(token));
        System.out.println("*********");
        if (!userOptional.isPresent()) {return "Invalid token.";}

        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();
        if (isTokenExpired(tokenCreationDate)) {
            return "Token expired.";
        }
        System.out.println(userOptional.get());
        User user = userOptional.get();
        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);
        return "Your password successfully updated.";
    }

    /**
     * Generate unique token. You may add multiple parameters to create a strongtoken.
     * @return unique token
     */
    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();}

    /**
     * Check whether the created token expired or not.
     * @param tokenCreationDate
     * @return true or false
     */
    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;}

}

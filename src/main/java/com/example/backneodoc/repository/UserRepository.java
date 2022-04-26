package com.example.backneodoc.repository;

import com.example.backneodoc.models.Document;
import com.example.backneodoc.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   // Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByFirstname(String firstname);
//List<User> findByDoc_favoris(Document doc);
    Boolean existsByEmail(String email);

    User findByEmail(String email) ;

    User findByToken(String token);

    List<User> findAllByEnabled(Boolean enabled, Sort by);

    @Query(value="select users.id,users.email,users.username,users.enabled,users.firstname,users.lastname,users.password,users.poste,users.token,users.token_creation_date,users.tokenCreationDate from users,document,favoris where document.id=?1 and users.id=favoris.id_user and document.id=favoris.id_document",nativeQuery=true)
    List<User> searchUserByFav(Long idd);

    Optional<User> findById(Long id);

    @Query(value= "DELETE FROM favoris WHERE favoris.id_user = ?1 AND favoris.id_document = ?2",nativeQuery=true)
    void delete(Long idu,Long idd);

}
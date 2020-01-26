package com.nilin.springboot.repository;

import com.nilin.springboot.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long>{
    Optional<Album> findById(Long albumId);
   // Collection<Album> getAlbumsBy_User_Username(String userUsername);
    Album findByuserUsername(String username); //findByuserUsername shoud be property in Album!
    List<Album> findAllByuserUsername(String UserUsername);
    List<Album> findAllByUserId(Long userId);

}


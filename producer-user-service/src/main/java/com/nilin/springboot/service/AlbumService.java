package com.nilin.springboot.service;

import com.nilin.springboot.model.Album;
import com.nilin.springboot.repository.AlbumRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;


    /**
     * Method to create a new album in the repository
     * HTTP : POST
     */

    @Transactional
    public Album createAlbum(String title, Long UserId) {
        Album Album = new Album(title, UserId);
        return this.albumRepository.save(Album);
    }


    /**
     * Method to delete the album from the repository by albumId
     * HTTP : DELETE
     */

    @Transactional
    public void deleteAlbum(Long id) {

        this.albumRepository.deleteById(id);
    }





    /**
     * Method to get all list of albums for a user
     *
     */
    public List<Album> getAlbumsByUserId(Long userId) {

        return this.albumRepository.findAllByUserId(userId);
    }





    /**
     * Method to get just profile album by userUsername
     */
    public Album getProfileAlbumByUserUsername(String UserUsername) {

        List<Album> userAlbums= this.albumRepository.findAllByuserUsername(UserUsername);
        Album profileAlbum;
        for (Album album:userAlbums) {
            String title = album.getTitle();
            if(title.equals("profile picture")) {
                return album;

            }
        }
        return null;
    }







    /**
     * Method to get a particular album using album id
     * HTTP : GET
     */

    public Album getAlbumById(Long id) {

        Optional<Album> Album = this.albumRepository.findById(id);

        if (Album.isPresent()) {
            return Album.get();
        } else {
            return null;
        }
    }


}

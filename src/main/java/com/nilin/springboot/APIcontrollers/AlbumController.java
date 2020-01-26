package com.nilin.springboot.APIcontrollers;

import com.google.gson.Gson;
import com.nilin.springboot.model.Album;
import com.nilin.springboot.model.User;
import com.nilin.springboot.security.JWTAuthorizationFilter;
import com.nilin.springboot.security.JwtTokenUtil;
import com.nilin.springboot.service.AlbumService;
import com.nilin.springboot.service.ImageStorageService;
import com.nilin.springboot.service.SecurityService;
import com.nilin.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ImageStorageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Method to create an album for specific user
     * POST : /createAlbum
     * post username in body
    */
    @PostMapping("/createAlbum")
    public String createAlbum(@ModelAttribute("album") @Valid Album album , HttpServletRequest req) {
        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
        User existing = userService.findByUsername(username);
        Long userId = existing.getId();

        Album myAlbum = albumService.createAlbum(album.getTitle() , userId);
        Gson gson = new Gson();
        String albumJson = gson.toJson(myAlbum);
        return "Hey " + username + "! your new album is created:\n" + "Album details : " + albumJson;

    }



    /**
     * Method to delete an album by albumId for specific user. Deleting an album leads to delele all images of it.
     * DELETE : /deleteAlbum
     *
     */
    //@DeleteMapping("/deleteAlbum/{id}")
    @PostMapping("/deleteAlbum")
    public String deleteAlbum(@RequestParam("id") Long id , HttpServletRequest req){

        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
        User existing = userService.findByUsername(username);
        Long userId = existing.getId();

        List<Album> userAlbums = albumService.getAlbumsByUserId(userId);
        for (Album album:userAlbums) {
            Long albumId = album.getId();
            if (albumId == id) {
                albumService.deleteAlbum(id);
                return "Album deleted.";
            }
        }
        return "There is not any album with this id in your list of albums!";

    }


//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    /**
     * Method to get all list of albums of user by userUsername// every one allowed:)
     * GET : /showListOfAlbums/{userUsername}
     *
     */
    @GetMapping("/showListOfAlbums/{userUsername}")
    public List<String>  showListOfAlbums(@PathVariable String userUsername){
        User existing = userService.findByUsername(userUsername);
        Long userId = existing.getId();
        if (existing == null) {
            //return "There is not an account registered with this username";
            return null;
        }

        else {
            Gson gson = new Gson();
            List<Album> userAlbums = albumService.getAlbumsByUserId(userId);
            List<String> jsonAlbums = new ArrayList<String>();
            for (Album album:userAlbums) {
                jsonAlbums.add(gson.toJson(album));
            }

            return jsonAlbums; //error: java heap space
        }
    }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
}





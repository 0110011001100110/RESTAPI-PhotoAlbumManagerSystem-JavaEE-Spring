//package com.nilin.springboot.APIcontrollers;
//
//import com.google.gson.Gson;
//import com.nilin.springboot.DTO.MessageDto;
//import com.nilin.springboot.model.Album;
//import com.nilin.springboot.model.User;
//import com.nilin.springboot.security.JwtTokenUtil;
//import com.nilin.springboot.service.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class AlbumController {
//
//    private static final Logger log = LoggerFactory.getLogger(AlbumController.class);
//
//    @Autowired
//    private AlbumService albumService;
//
//    @Autowired
//    private ImageStorageService imageService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private SecurityService securityService;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private MessageDto messageDto;
//
//    @Autowired
//    private RabbitMQProducerService rabbitMQProducerService;
//
//    private static int albumNum = 0;
//
//    /**
//     * Method to create an album for specific user
//     * POST : /createAlbum
//    */
//    @PostMapping("/createAlbum")
//    public String createAlbum(@ModelAttribute("album") @Valid Album album , HttpServletRequest req) {
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//        Long userId = existing.getId();
//
//        Album myAlbum = albumService.createAlbum(album.getTitle() , userId);
//        messageDto.setAction("createAlbum");
//        messageDto.setAlbumNum(++albumNum);
//        messageDto.setUserId(existing.getId());
//        rabbitMQProducerService.send(messageDto);
//        log.info("createAlbum_Action_Message sent");
//
//        Gson gson = new Gson();
//        String albumJson = gson.toJson(myAlbum);
//        return "Hey " + username + "! your new album is created:\n" + "Album details : " + albumJson;
//
//    }
//
//
//
//    /**
//     * Method to delete an album by albumId for specific user. Deleting an album leads to delele all images of it.
//     * DELETE : /deleteAlbum
//     *
//     */
//    //@DeleteMapping("/deleteAlbum/{id}")
//    @PostMapping("/deleteAlbum")
//    public String deleteAlbum(@RequestParam("id") Long id , HttpServletRequest req){
//
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//        Long userId = existing.getId();
//
//        List<Album> userAlbums = albumService.getAlbumsByUserId(userId);
//        for (Album album:userAlbums) {
//            Long albumId = album.getId();
//            if (albumId == id) {
//                albumService.deleteAlbum(id);
//                return "Album deleted.";
//            }
//        }
//        return "There is not any album with this id in your list of albums!";
//
//    }
//
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//
//    /**
//     * Method to get all list of albums of user by userUsername// every one allowed:)
//     * GET : /showListOfAlbums/{userUsername}
//     *
//     */
//    @GetMapping("/showListOfAlbums/{userUsername}")
//    public List<String>  showListOfAlbums(@PathVariable String userUsername){
//        User existing = userService.findByUsername(userUsername);
//        Long userId = existing.getId();
//        if (existing == null) {
//            //return "There is not an account registered with this username";
//            return null;
//        }
//
//        else {
//            Gson gson = new Gson();
//            List<Album> userAlbums = albumService.getAlbumsByUserId(userId);
//            List<String> jsonAlbums = new ArrayList<String>();
//            for (Album album:userAlbums) {
//                jsonAlbums.add(gson.toJson(album));
//            }
//
//            return jsonAlbums; //error: java heap space
//        }
//    }
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//}
//
//
//
//

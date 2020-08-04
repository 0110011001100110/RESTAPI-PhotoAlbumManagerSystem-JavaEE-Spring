//package com.nilin.springboot.APIcontrollers;
//
//import com.google.gson.Gson;
//import com.nilin.springboot.DTO.MessageDto;
//import com.nilin.springboot.model.Album;
//import com.nilin.springboot.model.Image;
//import com.nilin.springboot.model.User;
//import com.nilin.springboot.security.JwtTokenUtil;
//import com.nilin.springboot.service.AlbumService;
//import com.nilin.springboot.service.ImageStorageService;
//import com.nilin.springboot.service.RabbitMQProducerService;
//import com.nilin.springboot.service.UserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//public class ImageController {
//
//    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
//
//    @Autowired
//    private ImageStorageService imageStorageService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AlbumService albumService;
//
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private MessageDto messageDto;
//
//    @Autowired
//    RabbitMQProducerService rabbitMQProducerService;
//
//    private static int imageNum;
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    /**
//     * Method to upload image in a specific user album
//     * POST : /uploadImage
//     * post albumId in body too
//     */
//    @PostMapping("/uploadImage")
//    public String uploadImage(@RequestParam("pic") MultipartFile file , HttpServletRequest req , long albumId , String imageInfo) {
//
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//        Long userId = existing.getId();
//
//        List<Album> userAlbums = albumService.getAlbumsByUserId(userId);
//
//        for (Album album:userAlbums) {
//            Long id = album.getId();
//            if (albumId == id) {
//                //messageDto.setImageNum(++imageNum);
//                messageDto.setAction("uploadImage");
//                messageDto.setUserId(existing.getId());
//                rabbitMQProducerService.send(messageDto);
//                log.info("uploadImage_Action_Message sent");
//
//                return imageStorageService.uploadImage(file , albumId , imageInfo);
//            }
//        }
//        return "There is not any album with this id in your list of albums!";
//
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//    /**
//     * Method to upload images in a specific user album
//     * POST : /uploadMultipleImages
//     * post albumId in body
//     */
//    @PostMapping("/uploadMultipleImages")
//    public List<String> uploadMultipleImages(@RequestParam("pic") MultipartFile[] files , @ModelAttribute("user") @Valid User user , HttpServletRequest req , long albumId , String imageInfo) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadImage(file , req , albumId , imageInfo))
//                .collect(Collectors.toList());
//    }
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    @GetMapping("/downloadImage/{imageId}")
//    public ResponseEntity<Resource> downloadImage(@PathVariable String imageId) {
//        // Load file from database
//        Image image = imageStorageService.getImage(imageId);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(image.getImageType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; imagename=\"" + image.getImageName() + "\"")
//                .body(new ByteArrayResource(image.getData()));
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    /**
//     * Method to delete specific image from specific user album
//     * POST : /deleteImage
//     * post albumId in body too
//     */
//    @PostMapping("/deleteImage")
//    public String deleteImage(HttpServletRequest req , long albumId , String imageId) {
//
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//        Long userId = existing.getId();
//
//
//            List<Album> userAlbums = albumService.getAlbumsByUserId(userId);
//
//            for (Album album:userAlbums) {
//                Long id = album.getId();
//                if (albumId == id) {
//                    List<Image> albumImages = imageStorageService.getImagesByAlbumId(albumId);
//                    for (Image image:albumImages) {
//                        String photoId = image.getId();
//                        if(imageId.equals(photoId)) {
//                            imageStorageService.deleteImage(imageId);
//                            return "Image deleted";
//                        }
//                    }
//                    return "There is not any image with this id in your list of images!";
//                }
//            }
//            return "There is not any album with this id in your list of albums!";
//
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    /**
//     * Method to get all list of imageURLs of user by userUsername & albumId// everyone allowed:)
//     * GET : /showListOfImageURLs/{userUsername}/{albumId}
//     *
//     */
//    @GetMapping("/showListOfImageURLs/{userUsername}/{albumId}")
//    public List<String> showListOfImageURLs(@PathVariable String userUsername , @PathVariable Long albumId){
//
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
//            List<String> jsonImagesURL = new ArrayList<String>();
//
//            for (Album album:userAlbums) {
//                Long id = album.getId();
//                if (albumId == id) {
//                    List<Image> albumImages = imageStorageService.getImagesByAlbumId(albumId);
//                    for (Image image:albumImages) {
//                        String imageDownloadURL = imageStorageService.getImageDownloadUri(image);
//                        jsonImagesURL.add(gson.toJson(imageDownloadURL));
//                    }
//                    return jsonImagesURL;
//                }
//            }
//            //return "There is not any album with this id in your list of albums!";
//            return null;
//
//        }
//    }
//
//}
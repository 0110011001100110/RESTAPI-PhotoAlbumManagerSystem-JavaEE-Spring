package com.nilin.springboot.APIcontrollers;

import com.nilin.springboot.model.Album;
import com.nilin.springboot.model.Image;
import com.nilin.springboot.model.User;
import com.nilin.springboot.security.JWTAuthorizationFilter;
import com.nilin.springboot.security.JwtTokenUtil;
import com.nilin.springboot.service.*;
import com.nilin.springboot.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private AlbumService albumService;


/*
     @ModelAttribute annotations are used to convert dynamic request parameters into objects specified in Java annotations.
     For example, observing the code @ModelAttribute("article") Article article,Spring will try to match all request parameters
     to the field of the Article class. Now, suppose that this class has two fields: title and content. If the request contains
     title and content parameters, they will be used as the value of title and content for Article

 */

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @PostMapping("registration")
    public String  registerUserAccount(@ModelAttribute("user") @Valid User user) {

        User existing = userService.findByUsername(user.getUsername());
        if (existing != null) {
            return "There is already an account registered with this username";
        }
        else {
            userService.save(user);
            return "registration success ;)";
        }
    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @PostMapping("login")
    public String login(@ModelAttribute("user") @Valid User user) {

            User existing = userService.findByUsername(user.getUsername());
            if (existing == null) {
                return "There is not an account registered with this username";
            }
            else {
                if(user.getPassword().equals(existing.getPassword())) {

//                    final Authentication authentication = authenticationManager.authenticate(
//                            new UsernamePasswordAuthenticationToken(
//                                    user.getUsername(),
//                                    user.getPassword()
//                            )
//                    );
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    String token = JwtTokenUtil.generateToken(existing);

                    String token = securityService.getJWTToken(user.getUsername());
                    existing.setToken(token);
                    userService.save(existing);
                    return "login success :)\n" + "Your token is: " + token;
                    //return ResponseEntity.ok(new AuthToken(token));
                }
                else
                    return "username and password don't match :/";
            }
    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @PostMapping("logout") //??? get not supported
    public String logout(HttpServletRequest req) {

        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
        User existing = userService.findByUsername(username);

        if (existing == null) {
            return "There is not an account registered with this username at all!";
        }
        else {

            userService.deleteUserToken(existing);
            return username + " logged out and his token deleted!";
            }
    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * Method to create a userProfile (Create fullname, date, upload prof image)
     * POST : /createUserProfile/
     */
    @PostMapping("/createUserProfile")
    public String createUserProfile(@ModelAttribute("user") @Valid User user , HttpServletRequest req , @RequestParam("profilePic") MultipartFile file , @ModelAttribute("imageInfo") @Valid Image image) {

       String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
        User existing = userService.findByUsername(username);
        String imageInfo = image.getImageInfo();

        if(imageInfo == null)//If user does not want to enter image info
            imageInfo =" ";


        String response = userService.createUserProfile(existing, user , file , imageInfo);
        return username + " profile successfully created :)\n" + response;


        //return new ResponseEntity<>(this.userService.createUserProfile(user), HttpStatus.CREATED);

    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * Method to update a particular userProfile
     * Post : /updateUserProfile
     */
    @PostMapping("/updateUserProfile")
    public String updateUserProfile(@ModelAttribute("user") @Valid User user , HttpServletRequest req , @RequestParam("profilePic") MultipartFile file , @ModelAttribute("imageInfo") @Valid Image image) {

        String username =jwtTokenUtil.fetchUsernameFromHeaderToken(req);
        User existing = userService.findByUsername(username);

        return userService.updateUserProfile(existing , user , file , image.getImageInfo());

    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * Method to get userProfile(show) by username
     * POST : //showUserProfile/
     */
    @PostMapping("/showUserProfile")
    public String showUserProfileByUsername(HttpServletRequest req) {

        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
        User existing = userService.findByUsername(username);

        String fullname = existing.getFullname();
        Date dateOfBirth = existing.getDateOfBirth();
        Album userProfileImageAlbum = albumService.getProfileAlbumByUserUsername(username);
        Image userProfileImage = imageStorageService.getImageByAlbumId(userProfileImageAlbum.getId());

        String profilePictureURL = imageStorageService.getImageDownloadUri(userProfileImage);

        return "Hey " + username + "! your profile details:\n" + "fullname : " + fullname + "\ndateOfBirth : " + dateOfBirth
                    + "\nprofileImageURL : " + profilePictureURL
                    + "\nprofileImageInfo : " + userProfileImage.getImageInfo()
                    + "\nprofileImageId : " + userProfileImage.getId()
                    + "\nprofileImageAlbumId : " + userProfileImageAlbum.getId();

        }
    }

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


//package com.nilin.springboot.APIcontrollers;
//
//import com.nilin.springboot.DTO.MessageDto;
//import com.nilin.springboot.FeignInterfaces.ProducerFeignClient;
//import com.nilin.springboot.service.RabbitMQProducerService;
//import com.nilin.springboot.model.Album;
//import com.nilin.springboot.model.Image;
//import com.nilin.springboot.model.User;
////import com.nilin.springboot.security.JwtTokenUtil;
//import com.nilin.springboot.service.*;
//import com.nilin.springboot.validator.UserValidator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@RestController
//public class UserController {
//
//    private static final Logger log = LoggerFactory.getLogger(UserController.class);
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private SecurityService securityService;
//
//    @Autowired
//    private UserValidator userValidator;
//
//
////    @Autowired
////    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private ImageStorageService imageStorageService;
//
//    @Autowired
//    private AlbumService albumService;
//
//    @Autowired
//    private MessageDto messageDto;
//
//    @Autowired
//    private RabbitMQProducerService rabbitMQProducerService;
//
////    @Autowired
////    RestTemplate restTemplate;
//
//    @Autowired
//    ProducerFeignClient producerFeignClient;
//
///*
//     @ModelAttribute annotations are used to convert dynamic request parameters into objects specified in Java annotations.
//     For example, observing the code @ModelAttribute("article") Article article,Spring will try to match all request parameters
//     to the field of the Article class. Now, suppose that this class has two fields: title and content. If the request contains
//     title and content parameters, they will be used as the value of title and content for Article
//
// */
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    @PostMapping("registration")
//    public String registerUserAccount(@ModelAttribute("user") @Valid User user) {
//
//        User existing = userService.findByUsername(user.getUsername());
//        if (existing != null) {
//            return "There is already an account registered with this username";
//        } else {
////            LocalDateTime registrationTime = LocalDateTime.now();
//            messageDto.setAction("Registration");
//            Date date = new Date();
//            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            String registrationTime = formatter.format(date);
//            messageDto.setRegistrationTime(registrationTime);
//           // rabbitMQProducerService.send("hi rabbit");
//
//            userService.save(user);
//            User registered = userService.findByUsername(user.getUsername());
//            messageDto.setUserId(registered.getId());
//            rabbitMQProducerService.send(messageDto);
//            log.info("Registration_Action_Message sent");
//            System.out.print(messageDto);
//            return "registration success ;)";
//        }
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
////    @PostMapping("login") //transfered to  athorization service
////    public String login(@ModelAttribute("user") @Valid User user) {
////
////        User existing = userService.findByUsername(user.getUsername());
////        if (existing == null) {
////            return "There is not an account registered with this username";
////        } else {
////            if (user.getPassword().equals(existing.getPassword())) {
////
//////                    final Authentication authentication = authenticationManager.authenticate(
//////                            new UsernamePasswordAuthenticationToken(
//////                                    user.getUsername(),
//////                                    user.getPassword()
//////                            )
//////                    );
//////                    SecurityContextHolder.getContext().setAuthentication(authentication);
//////                    String token = JwtTokenUtil.generateToken(existing);
////
////                String token = securityService.getJWTToken(user.getUsername());
////                existing.setToken(token);
////                userService.save(existing);
////                return "login success :)\n" + "Your token is: " + token;
////                //return ResponseEntity.ok(new AuthToken(token));
////            } else
////                return "username and password don't match :/";
////        }
////    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    @PostMapping("logout") //??? get not supported
//    public String logout(HttpServletRequest req) {
//
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//
//        if (existing == null) {
//            return "There is not an account registered with this username at all!";
//        } else {
//
//            userService.deleteUserToken(existing);
//            return username + " logged out and his token deleted!";
//        }
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    /**
//     * Method to create a userProfile (Create fullname, date, upload prof image)
//     * POST : /createUserProfile/
//     */
//    @PostMapping("/createUserProfile")
//    public String createUserProfile(@ModelAttribute("user") @Valid User user, HttpServletRequest req, @RequestParam("profilePic") MultipartFile file, @ModelAttribute("imageInfo") @Valid Image image) {
//
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//        String imageInfo = image.getImageInfo();
//
//        if (imageInfo == null)//If user does not want to enter image info
//            imageInfo = " ";
//
//
//        String response = userService.createUserProfile(existing, user, file, imageInfo);
//        return username + " profile successfully created :)\n" + response;
//
//
//        //return new ResponseEntity<>(this.userService.createUserProfile(user), HttpStatus.CREATED);
//
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    /**
//     * Method to update a particular userProfile
//     * Post : /updateUserProfile
//     */
//    @PostMapping("/updateUserProfile")
//    public String updateUserProfile(@ModelAttribute("user") @Valid User user, HttpServletRequest req, @RequestParam("profilePic") MultipartFile file, @ModelAttribute("imageInfo") @Valid Image image) {
//
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//
//        return userService.updateUserProfile(existing, user, file, image.getImageInfo());
//
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    /**
//     * Method to get userProfile(show) by username
//     * POST : //showUserProfile/
//     */
//    @PostMapping("/showUserProfile")
//    public String showUserProfileByUsername(HttpServletRequest req) {
//
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//
//        String fullname = existing.getFullname();
//        Date dateOfBirth = existing.getDateOfBirth();
//        Album userProfileImageAlbum = albumService.getProfileAlbumByUserUsername(username);
//        Image userProfileImage = imageStorageService.getImageByAlbumId(userProfileImageAlbum.getId());
//
//        String profilePictureURL = imageStorageService.getImageDownloadUri(userProfileImage);
//
//        return "Hey " + username + "! your profile details:\n" + "fullname : " + fullname + "\ndateOfBirth : " + dateOfBirth
//                + "\nprofileImageURL : " + profilePictureURL
//                + "\nprofileImageInfo : " + userProfileImage.getImageInfo()
//                + "\nprofileImageId : " + userProfileImage.getId()
//                + "\nprofileImageAlbumId : " + userProfileImageAlbum.getId();
//
//    }
//
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    /**
//     * Method to get getCurrentScore by userId
//     * POST : //getCurrentScore/
//     */
//    @PostMapping("/getCurrentScore")
//    public String getCurrentScore(HttpServletRequest req) {
//
//        //final String url = "http://localhost:2088/getCurrentScore";
//        final String url = "http://consumer-service/getCurrentScore"; //useful for resttemplate
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//        Long userId = existing.getId();
//
//        /**
//         * Rest Template
//         */
////       HttpEntity<Long> entity = new HttpEntity<Long>(userId);
////       ResponseEntity<String> currentScore = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//       //String currentScore = restTemplate.postForObject(url, userId, String.class);
//
//
//        /**
//         * Feign Client
//         */
//        String  currentScore = producerFeignClient.getCurrentScore(userId);
//        return currentScore;
//
//    }
//
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//    /**
//     * Method to get getAccumulativeInfos by userId
//     * POST : //getAccumulativeInfo/
//     */
//    @PostMapping("/getAccumulativeInfo")
//    public String getAccumulativeInfo(HttpServletRequest req) {
//
//        //final String url = "http://localhost:8081/getAccumulativeInfo";
//        final String url = "http://consumer-service/getAccumulativeInfo"; //useful for resttemplate
//        String username = jwtTokenUtil.fetchUsernameFromHeaderToken(req);
//        User existing = userService.findByUsername(username);
//        Long userId = existing.getId();
//
//        /**
//         * Rest Template
//         */
//        //String accumulativeInfo = restTemplate.postForObject(url, userId, String.class);
////        HttpEntity<Long> entity = new HttpEntity<Long>(userId);
////        ResponseEntity<String> accumulativeInfo = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//
//        /**
//         * Feign Client
//         */
////        String accumulativeInfo = producerFeignClient.getAccumulativeInfo(userId);
////        return accumulativeInfo;
//       return userService.getAccumulativeInfo(producerFeignClient , userId);
//    }
//}

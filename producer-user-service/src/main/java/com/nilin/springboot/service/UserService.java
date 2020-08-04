package com.nilin.springboot.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.nilin.springboot.FeignInterfaces.ProducerFeignClient;
import com.nilin.springboot.model.Album;
import com.nilin.springboot.model.Image;
import com.nilin.springboot.model.User;
import com.nilin.springboot.repository.RoleRepository;
import com.nilin.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private AlbumService albumService;


    public void save(User user) {
//        user.setPasswordConfirm(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }


    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }


    public void deleteUserToken(User user) {

        user.setToken(null);
        userRepository.save(user);

    }


    /**
     * Method to create a new userProfile in the repository
     * HTTP : POST
     */

    @Transactional
    public String createUserProfile(User existing, User user, MultipartFile file, String imageInfo) {

        existing.setFullname(user.getFullname());
        existing.setDateOfBirth(user.getDateOfBirth());
        Album album = albumService.createAlbum("profile picture", existing.getId()); //Album which belongs to username

        this.userRepository.save(existing);
        return imageStorageService.uploadImage(file, album.getId(), imageInfo);

    }


    @Transactional
    public String updateUserProfile(User existing, User user, MultipartFile file, String imageInfo) {

        //flag = true ---> profile updated!
        boolean flag = false;

        Album userProfileImageAlbum = albumService.getProfileAlbumByUserUsername(user.getUsername());
        Image userProfileImage = imageStorageService.getImageByAlbumId(userProfileImageAlbum.getId());

        if (user.getFullname() != null) {// It means user wanna update fullname and post new fullname param
            existing.setFullname(user.getFullname());
            flag = true;
        }


        if (user.getDateOfBirth() != null) {// It means user wanna update dateOfBirth and post new dateOfBirth param
            existing.setDateOfBirth(user.getDateOfBirth());
            flag = true;
        }

        this.userRepository.save(existing);


        if (file != null && imageInfo == null) {//Just upload new profile image

            imageStorageService.uploadUpdatedImage(file, userProfileImageAlbum.getId(), imageInfo, userProfileImage);
            flag = true;
        }


        if (file != null && imageInfo != null) {//Set new image info and upload new profile image

            userProfileImage.setImageInfo(imageInfo);
            imageStorageService.uploadUpdatedImage(file, userProfileImageAlbum.getId(), userProfileImage.getImageInfo(), userProfileImage); //set new image for profile
            flag = true;
        }

        if (file == null && imageInfo != null) {//Just set new image info

            userProfileImage.setImageInfo(imageInfo);
            imageStorageService.storeImage(userProfileImage);
            flag = true;
        }

        if (flag)
            return "Your profile updated!";
        else
            return "You don't update your profile!";
    }


    /**
     * Method to get a particular userProfile using username
     * HTTP : GET
     */

    public User getUserProfileByUserName(String username) {

        User user = this.userRepository.findByUsername(username);
        return user;
    }


    @Bean
    @LoadBalanced //not catch unknown host  "http://consumer-service/getAccumulativeInfo (for discovering is necessary)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @HystrixCommand(fallbackMethod = "getAccumulativeInfo_Fallback")
    public String getAccumulativeInfo(ProducerFeignClient producerFeignClient, Long userId) {

        /**
         * Feign Client
         */
        String accumulativeInfo = producerFeignClient.getAccumulativeInfo(userId);
        return "Consumer Service is up\n" + accumulativeInfo;
    }



    private String getAccumulativeInfo_Fallback(ProducerFeignClient producerFeignClient, Long userId) {

//        return "CIRCUIT BREAKER ENABLED!!! No Response From Consumer Service at this moment. " +
//                " Service will be back shortly";
        return "Consumer Service is down! fallback route enabled";


    }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
}
//Provide service for registering account
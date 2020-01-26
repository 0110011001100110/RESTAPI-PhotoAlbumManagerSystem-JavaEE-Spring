package com.nilin.springboot.service;

import com.nilin.springboot.model.Album;
import com.nilin.springboot.model.Image;
import com.nilin.springboot.model.User;
import com.nilin.springboot.repository.RoleRepository;
import com.nilin.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private com.nilin.springboot.security.JwtTokenUtil JwtTokenUtil;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private AlbumService albumService;


    public void save(User user) {
        user.setPasswordConfirm(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }




    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }


    public void deleteUserToken(User user){

        user.setToken(null);
        userRepository.save(user);

    }



    /**
     * Method to create a new userProfile in the repository
     * HTTP : POST
     *
     */

    @Transactional
    public String createUserProfile(User existing , User user , MultipartFile file , String imageInfo){

        existing.setFullname(user.getFullname());
        existing.setDateOfBirth(user.getDateOfBirth());
        Album album = albumService.createAlbum("profile picture" , existing.getId()); //Album which belongs to username

        this.userRepository.save(existing);
        return imageStorageService.uploadImage(file , album.getId() , imageInfo);

    }



    @Transactional
    public String updateUserProfile(User existing , User user , MultipartFile file , String imageInfo){

        //flag = true ---> profile updated!
        boolean flag = false;

        Album userProfileImageAlbum = albumService.getProfileAlbumByUserUsername(user.getUsername());
        Image userProfileImage = imageStorageService.getImageByAlbumId(userProfileImageAlbum.getId());

        if(user.getFullname() != null) {// It means user wanna update fullname and post new fullname param
            existing.setFullname(user.getFullname());
            flag = true;
        }


        if(user.getDateOfBirth() != null) {// It means user wanna update dateOfBirth and post new dateOfBirth param
            existing.setDateOfBirth(user.getDateOfBirth());
            flag = true;
        }

        this.userRepository.save(existing);


        if(file != null && imageInfo == null) {//Just upload new profile image

            imageStorageService.uploadUpdatedImage(file , userProfileImageAlbum.getId() , imageInfo , userProfileImage);
            flag = true;
        }



        if(file != null && imageInfo != null){//Set new image info and upload new profile image

            userProfileImage.setImageInfo(imageInfo);
            imageStorageService.uploadUpdatedImage(file , userProfileImageAlbum.getId() , userProfileImage.getImageInfo() , userProfileImage); //set new image for profile
            flag = true;
        }

        if(file == null && imageInfo != null){//Just set new image info

            userProfileImage.setImageInfo(imageInfo);
            imageStorageService.storeImage(userProfileImage);
            flag = true;
        }

        if(flag)
            return "Your profile updated!";
        else
            return "You don't update your profile!";
    }


    /**
     * Method to get a particular userProfile using username
     * HTTP : GET
     *
     */

    public User getUserProfileByUserName(String username) {

        User user = this.userRepository.findByUsername(username);
        return user;
    }





}
//Provide service for registering account
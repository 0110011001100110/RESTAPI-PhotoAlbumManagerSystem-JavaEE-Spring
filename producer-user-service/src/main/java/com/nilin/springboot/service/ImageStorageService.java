package com.nilin.springboot.service;

import com.google.gson.Gson;
import com.nilin.springboot.exception.FileStorageException;
import com.nilin.springboot.exception.MyFileNotFoundException;
import com.nilin.springboot.model.Image;
import com.nilin.springboot.payload.UploadFileResponse;
import com.nilin.springboot.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

//contains methods to store and retrieve files to/from the database
@Service
public class ImageStorageService {

    @Autowired
    private ImageRepository imageRepository;




    public Image storeImage(MultipartFile file , long albumId , String imageInfo) {
        // Normalize file name
        String imageName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(imageName.contains("..")) {
                throw new FileStorageException("Sorry! imageName contains invalid path sequence " + imageName);
            }

            Image image = new Image(imageName, file.getContentType(), file.getBytes() , albumId , imageInfo);

            return imageRepository.save(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
    }



    public Image storeUpdatedImage(MultipartFile file , long albumId , String imageInfo , Image oldImage) {
        // Normalize file name
        String imageName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(imageName.contains("..")) {
                throw new FileStorageException("Sorry! imageName contains invalid path sequence " + imageName);
            }

            //Image image = new Image(imageName, file.getContentType(), file.getBytes() , albumId , imageInfo);
            oldImage.setImageName(imageName);
            oldImage.setImageType(file.getContentType());
            oldImage.setData(file.getBytes());
            oldImage.setImageInfo(imageInfo);
            return imageRepository.save(oldImage);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!", ex);
        }
    }




    public void storeImage(Image image) {

        imageRepository.save(image);
    }




    public Image getImageByAlbumId(Long id){
        return imageRepository.findByalbumId(id)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + id));

    }



    /**
     * Method to get all list of albums for a user
     *
     */
    public List<Image> getImagesByAlbumId(Long albumId) {

        return this.imageRepository.findAllByalbumId(albumId);
    }



    public Image getImage(String imageId) {
        return imageRepository.findByimageId(imageId);
               // .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + imageId));
    }





    public String uploadImage(MultipartFile file , long albumId , String imageInfo) {
        Image image = this.storeImage(file , albumId , imageInfo);

        String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadImage/")
                .path(image.getId())
                .toUriString();

        UploadFileResponse UploadFileResponse = new UploadFileResponse(image.getImageName(), image.getId(), albumId , imageDownloadUri,
                                                                                file.getContentType(), file.getSize());
        Gson gson = new Gson();
        String json = gson.toJson(UploadFileResponse);

        return json;
    }




    public String uploadUpdatedImage(MultipartFile file , long albumId , String imageInfo , Image oldImage) {
        Image image = this.storeUpdatedImage(file , albumId , imageInfo , oldImage);

        String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadImage/")
                .path(image.getId())
                .toUriString();

        UploadFileResponse UploadFileResponse = new UploadFileResponse(image.getImageName(), image.getId(), albumId , imageDownloadUri,
                file.getContentType(), file.getSize());
        Gson gson = new Gson();
        String json = gson.toJson(UploadFileResponse);

        return json;
    }



    public String getImageDownloadUri(Image image){
        String imageDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadImage/")
                .path(image.getId())
                .toUriString();
        return imageDownloadUri;
    }


    /**
     * Method to delete the image from the repository by imageId
     * HTTP : DELETE
     */

    @Transactional
    public void deleteImage(String id)
    {
        this.imageRepository.deleteById(id);
    }





}






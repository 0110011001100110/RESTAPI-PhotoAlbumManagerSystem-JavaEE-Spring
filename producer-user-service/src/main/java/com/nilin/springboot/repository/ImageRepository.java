package com.nilin.springboot.repository;

import com.nilin.springboot.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    Optional<Image> findByalbumId(Long id); // findByalbumId is attrib in image
    Image findByimageId(String imageId);
    List<Image> findAllByalbumId(Long albumId);

}

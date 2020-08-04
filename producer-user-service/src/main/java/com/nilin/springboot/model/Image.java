package com.nilin.springboot.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "photos")
public class Image {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String imageId;

    private String imageName;

    private String imageType;

    private String imageInfo;

    private Long albumId; //:albumId: foreign key to Album

    @Lob
    private byte[] data;


    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "albumId" , insertable=false, updatable=false)

    private Album album;

    public Image() {

    }

    public Image(String imageName, String imageType, byte[] data , long albumId , String imageInfo) {
        this.imageName = imageName;
        this.imageType = imageType;
        this.data = data;
        this.albumId = albumId;
        this.imageInfo = imageInfo;
    }

    public String getId() {
        return imageId;
    }
    public void setId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getImageType() {
        return imageType;
    }
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }


    public String getImageInfo() {
        return imageInfo;
    }
    public void setImageInfo(String imageInfo) {
        this.imageInfo = imageInfo;
    }


    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }

    public void setAlbum(Album album) { this.album = album; }
    public Album getAlbum() { return album; }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
    public Long getAlbumId() {
        return albumId;
    }

}

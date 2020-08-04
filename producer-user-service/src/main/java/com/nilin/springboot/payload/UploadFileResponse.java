package com.nilin.springboot.payload;


public class UploadFileResponse {
    private String imageName;
    private String imageId;
    private long albumId;
    private String imageDownloadUri;
    private String imageType;
    private long size;

    public UploadFileResponse(String imageName, String imageId, long albumId , String imageDownloadUri, String imageType, long size) {
        this.imageName = imageName;
        this.imageId = imageId;
        this.albumId = albumId;
        this.imageDownloadUri = imageDownloadUri;
        this.imageType = imageType;
        this.size = size;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageDownloadUri() {
        return imageDownloadUri;
    }

    public void setImageDownloadUri(String imageDownloadUri) {
        this.imageDownloadUri = imageDownloadUri;
    }

    public String getimageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}

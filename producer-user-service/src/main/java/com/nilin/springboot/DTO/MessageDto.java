package com.nilin.springboot.DTO;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,property = "@id",scope = MessageDto.class)
@Data
@NoArgsConstructor
@Component
public class MessageDto implements Serializable {

    //@JsonProperty
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String registrationTime;

    private int albumNum;

    private String action;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String  getRegistrationTime() {
        return registrationTime;
    }
    public void setRegistrationTime(String  registrationTime) {
        this.registrationTime = registrationTime;
    }


    public int getAlbumNum() {
        return albumNum;
    }
    public void setAlbumNum(int albumNum) {
        this.albumNum = albumNum;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}



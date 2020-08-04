package com.nilin.springboot.APIcontroller;

import com.nilin.springboot.model.UserScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.nilin.springboot.service.RabbitMQConsumerService;

import java.time.LocalDateTime;

@RestController
public class UserScoreController {

    @Autowired
    private RabbitMQConsumerService rabbitMQConsumerService;


    //post userId in body
    //without authentication
    @PostMapping("/getCurrentScore")
    public String getCurrentScore(@RequestBody Long userId){

        UserScore userScore = rabbitMQConsumerService.getUserScoreById(userId);
        int currentScore = userScore.getScore();

        return "Your current score is: " + currentScore;
        //return userId.toString();
    }


    //AccumulativeInfo -> dateOfRegistration , albumNum , userScore
    //without authentication
    @PostMapping("/getAccumulativeInfo")
    public String getAccumulativeInfo(@RequestBody Long userId){

        UserScore userScore = rabbitMQConsumerService.getUserScoreById(userId);
        String dateOfRegistration = userScore.getDateTime();
        int albumNum = userScore.getNumberOfAlbums();
        int currentScore = userScore.getScore();

        return "Your dateOfRegistration is: " + dateOfRegistration + "\n" +
                "Your albumNum is: " + albumNum + "\n" +
                "Your currentScore is: " + currentScore;
    }
}

package com.nilin.springboot.service;

import com.google.gson.Gson;
import com.nilin.springboot.DTO.MessageDto;
import com.nilin.springboot.model.UserScore;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilin.springboot.repository.UserScoreRepository;

import java.util.Optional;

@Service
@EnableRabbit
public class RabbitMQConsumerService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumerService.class);

    @Autowired
    private MessageDto messageDto;

    @Autowired
    private UserScoreRepository userScoreRepository;

    @Autowired
    private UserScore userScore;


    @RabbitListener(queues="${queue.name}")
    public void receiveAndProcess(MessageDto msgDto) {

        String action = msgDto.getAction();

        if(action.equals("Registration")){

            log.info("Registration_Action_Message received");
            messageDto.setUserId(msgDto.getUserId());
            messageDto.setRegistrationTime(msgDto.getRegistrationTime());
            messageDto.setAction("Registration");
            registerScore(messageDto);

        }

        else if(action.equals("createAlbum")){

            log.info("createAlbum_Action_Message received");
            messageDto.setUserId(msgDto.getUserId());
            messageDto.setAlbumNum(msgDto.getAlbumNum());
            messageDto.setAction("createAlbum");
            registerScore(messageDto);

        }

        else if(action.equals("uploadImage")){

            log.info("uploadImage_Action_Message received");
            messageDto.setUserId(msgDto.getUserId());
            messageDto.setAction("uploadImage");
            registerScore(messageDto);

        }

        //log.info("Message received after if-clauses");
    }


    public void registerScore(MessageDto msgDto){

        String action = msgDto.getAction();

        if(action.equals("Registration")){

            userScore.setUserId(msgDto.getUserId());
            userScore.setDateTime(msgDto.getRegistrationTime());
            userScore.setScore(5);
            saveUserScore(userScore);
        }

        else if(action.equals("createAlbum")){

            UserScore existing = getUserScoreById(msgDto.getUserId());
            //existing.setUserId(msgDto.getUserId());
            existing.setNumberOfAlbums(msgDto.getAlbumNum());
            int previousScore = existing.getScore();
            int score = previousScore + msgDto.getAlbumNum();
            existing.setScore(score);
            saveUserScore(existing);
        }

        else if(action.equals("uploadImage")){

            UserScore existing = this.getUserScoreById(msgDto.getUserId());
            //existing.setUserId(msgDto.getUserId());
            int previousScore = existing.getScore();
            int score = (previousScore + existing.getNumberOfAlbums()) * 2;
            existing.setScore(score);
            saveUserScore(existing);
        }
    }

    public void saveUserScore(UserScore userScore){

        userScoreRepository.save(userScore);
   }



    public UserScore getUserScoreById(Long id) {

        Optional<UserScore> userScore = userScoreRepository.findByUserId(id);

        if (userScore.isPresent()) {
            return userScore.get();
        } else {
            return null;
        }
    }


//@RabbitListener(queues="${queue.name}")
//    public void receiveAndProcessMessage(String message){
//
//        log.info("Received message as generic: {}", message.toString());
//    }
}

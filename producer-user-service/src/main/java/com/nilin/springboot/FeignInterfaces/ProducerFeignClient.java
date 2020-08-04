package com.nilin.springboot.FeignInterfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "consumer-service", url = "http://localhost:8081") //without spring cloud(consul)
@FeignClient(name = "consumer-service") //destination microservice name
public interface ProducerFeignClient {

    @PostMapping("/getCurrentScore") //in destination microservice
    public String getCurrentScore(@RequestBody Long userId);

    @PostMapping("/getAccumulativeInfo") ////in destination microservice
    public String getAccumulativeInfo(@RequestBody Long userId);

}


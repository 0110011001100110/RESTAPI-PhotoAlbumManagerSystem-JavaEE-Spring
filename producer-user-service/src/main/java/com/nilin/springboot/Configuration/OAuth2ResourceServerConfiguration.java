package com.nilin.springboot.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


@Configuration
@EnableResourceServer

public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {

            http.authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/login" , "/registration").permitAll()
                    .antMatchers(HttpMethod.GET, "/showListOfAlbums/{^[\\w]$}" , "/showListOfImageURLs/{^[\\w]$}/{^[\\d]$}" , "/downloadImage/{^[\\w]$}" /*"/actuator/health*/).permitAll()
                    .anyRequest().authenticated();
    }
}






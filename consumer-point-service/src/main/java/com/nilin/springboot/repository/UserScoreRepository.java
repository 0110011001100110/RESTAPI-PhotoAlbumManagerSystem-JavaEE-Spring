package com.nilin.springboot.repository;

import com.nilin.springboot.model.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserScoreRepository extends JpaRepository<UserScore, Long>{

    Optional<UserScore> findByUserId(Long id);

}


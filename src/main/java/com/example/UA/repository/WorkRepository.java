package com.example.UA.repository;

import com.example.UA.repository.entity.Work;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository {
    List<Work> findAll();
}

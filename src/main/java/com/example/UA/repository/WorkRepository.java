package com.example.UA.repository;

import com.example.UA.repository.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    @Query("SELECT w FROM Work w WHERE date BETWEEN :start AND :end ORDER BY date")
    public List<Work> findByStartAndEnd(@Param("start") Date start, @Param("end") Date end);
}

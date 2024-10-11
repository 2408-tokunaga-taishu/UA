package com.example.UA.repository;

import com.example.UA.repository.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    @Query("SELECT w FROM Work w WHERE date BETWEEN :start AND :end ORDER BY date")
    public List<Work> findByStartAndEnd(@Param("start") Date start, @Param("end") Date end);

    @Modifying
    @Query("UPDATE Work  SET status = 1, updatedDate = :Date WHERE id = :id")
    void saveStatus(@Param("id") int id, @Param("Date") Date Date);

    @Query("SELECT w FROM Work w WHERE groupId = :groupId AND status = 1  ORDER BY date")
    List<Work> findBygroupIdBystatus(@Param("groupId") Integer groupId);
}

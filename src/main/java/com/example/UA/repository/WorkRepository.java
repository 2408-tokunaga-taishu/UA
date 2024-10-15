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
//　申請済み勤怠数取得
    @Query("SELECT COUNT (w)  FROM Work w WHERE groupId = :groupId AND status = 1")
    int countBy(@Param("groupId") Integer groupId);
//　差し戻し勤怠数取得
    @Query("SELECT COUNT (w)  FROM Work w WHERE accountId = :id AND status = 3")
    int countByRemand(@Param("id") Integer id);
//  申請済み勤怠取得
    @Query("SELECT w FROM Work w WHERE groupId = :groupId AND status = 1")
    List<Work> findByGroupStatus(@Param("groupId") Integer groupId);
//  勤怠のstatus承認に変更
    @Modifying
    @Query("UPDATE Work  SET status = 2, updatedDate = :Date WHERE id = :id")
    void approval(@Param("id") int id, @Param("Date") Date date);
//  勤怠のstatus差し戻しに変更
    @Modifying
    @Query("UPDATE Work  SET status = 3, updatedDate = :Date WHERE id = :id")
    void remand(@Param("id") int id, @Param("Date") Date date);
}

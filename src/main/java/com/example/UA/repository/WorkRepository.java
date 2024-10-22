package com.example.UA.repository;

import com.example.UA.repository.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Transactional
@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    @Query("SELECT w FROM Work w WHERE date BETWEEN :start AND :end ORDER BY date")
    public List<Work> findByStartAndEnd(@Param("start") Date start, @Param("end") Date end);

    @Modifying
    @Query("UPDATE Work  SET status = 1, updatedDate = :Date, remandText = null WHERE id = :id")
    void saveStatus(@Param("id") int id, @Param("Date") Date Date);
//　申請済み勤怠数取得
    @Query("SELECT COUNT (w)  FROM Work w WHERE date BETWEEN :start AND :end AND groupId = :groupId AND status = 1")
    int countBy(@Param("groupId") Integer groupId, @Param("start") Date start, @Param("end") Date end);
//　差し戻し勤怠数取得
    @Query("SELECT COUNT (w)  FROM Work w WHERE  date BETWEEN :start AND :end AND accountId = :id AND status = 3")
    int countByRemand(@Param("id") Integer id, @Param("start") Date start, @Param("end") Date end);
//  申請済み勤怠取得
    @Query("SELECT w FROM Work w WHERE date BETWEEN :start AND :end AND groupId = :groupId AND status = 1 ORDER BY date ASC")
    List<Work> findByGroupStatus(@Param("groupId") Integer groupId, @Param("start") Date start, @Param("end") Date end);
//  勤怠のstatus承認に変更
    @Modifying
    @Query("UPDATE Work  SET status = 2, updatedDate = :Date WHERE id = :id")
    void approval(@Param("id") int id, @Param("Date") Date date);
//  勤怠のstatus差し戻しに変更
    @Modifying
    @Query("UPDATE Work  SET status = 3, updatedDate = :Date, remandText = :remandText WHERE id = :id")
    void remand(@Param("id") int id, @Param("Date") Date date, @Param("remandText") String remandText);

    // 個人別の勤怠集計
    @Query("SELECT w FROM Work w WHERE w.accountId = :accountId AND w.date BETWEEN :start AND :end")
    List<Work> findWorksByAccountId(@Param("accountId") int id, @Param("start") Date start, @Param("end") Date end);
//    打刻ボタン出勤
    @Modifying
    @Query("INSERT INTO Work (date, workStart, stamp, groupId, accountId, rest ) VALUES (:nowDate, :workStart, 1, :groupId, :accountId, :rest)")
    void stampWorkStart(@Param("nowDate") Date nowDate, @Param("workStart") Time workStart, @Param("groupId") int groupId, @Param("accountId") int accountId, @Param("rest") Time rest);

//    打刻ボタン退勤
    @Modifying
    @Query("UPDATE Work  SET workEnd = :workEnd, updatedDate = :Date, stamp = 0 WHERE stamp = 1 AND accountId =:id")
    void stampWorkEnd(@Param("workEnd") Time workEnd, @Param("Date") Date updateDate, @Param("id") int id);

    //  打刻ボタン休憩開始
    @Modifying
    @Query("UPDATE Work  SET restStart = :restStart, updatedDate = :Date, restStamp = 1 WHERE stamp = 1 AND accountId =:id ")
    void stampRestStart(@Param("restStart") Time restStart, @Param("Date") Date date, @Param("id") int id);

    @Modifying
    @Query("UPDATE Work  SET restEnd = :restEnd, updatedDate = :Date, restStamp = 2 WHERE stamp = 1 AND accountId =:id")
    void stampRestEnd(@Param("restEnd") Time restEnd, @Param("Date") Date date, @Param("id") int id);
//    休憩時間更新
    @Modifying
    @Query("UPDATE Work  SET rest = :rest  WHERE stamp = 1 AND accountId =:id")
    void calculateRest(@Param("rest") Time rest, @Param("id") int id);

    @Query("SELECT w FROM Work w WHERE stamp = 1 AND accountId =:id")
    Work findbyStamp(@Param("id") int id);
}

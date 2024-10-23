package com.example.UA.repository;


import com.example.UA.repository.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository

public interface WorkLogRepository extends JpaRepository<WorkLog, Integer> {
//    出勤buttonログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 1)")
    void workStartLog(@Param("id") int id);

//    退勤buttonログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 2)")
    void workEndLog(@Param("id") int id);

//    休憩開始ログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 3)")
    void restStartLog(@Param("id") int id);
//    休憩終了ログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 4)")
    void restEndLog(@Param("id")int id);
//    手動打刻
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 5)")
    void manualWork(int id);
//    勤怠の編集ログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 6)")
    void workEditLog(@Param("id")int id);

//　勤怠削除に伴うログの削除
   void deleteByWorkId(int id);

//    勤怠申請のログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 7)")
    void requestWork(@Param("id")int id);

//    勤怠の申請ログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 8)")
    void approvalWork(@Param("id")int id);
//　　勤怠の差し戻しログ
    @Modifying
    @Query("INSERT INTO WorkLog (workId, content) VALUES (:id, 9)")
    void remandWork(@Param("id")int id);
}

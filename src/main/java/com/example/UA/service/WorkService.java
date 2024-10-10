package com.example.UA.service;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.WorkForm;
import com.example.UA.repository.WorkRepository;
import com.example.UA.repository.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static org.apache.logging.log4j.util.Strings.isBlank;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Date;
import java.util.List;

@Service
public class WorkService {
    @Autowired
    WorkRepository workRepository;
    // 日付の操作で各メソッドで使用するのでここに記述
    private LocalDate displayMonth = LocalDate.now();

    /*
     * 勤怠記録全取得(月単位)
     */
    public List<WorkForm> findAllWorks() throws ParseException {
        List<Work> results = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 指定した月の1日目と最終日の取得
        LocalDate LdStart = displayMonth.withDayOfMonth(1); //現在の月の1日を指す
        LocalDate LdEnd = displayMonth.withDayOfMonth(displayMonth.lengthOfMonth()); //lengthOfMonthは月の長さを日数で返す
        //　JpaRepositoryで処理ができるように形成
        String startStr = LdStart + " 00:00:00";
        String endStr = LdEnd + " 23:59:59";
        Date start = sdf.parse(startStr);
        Date end = sdf.parse(endStr);
        results = workRepository.findByStartAndEnd(start, end);
        List<WorkForm> works = setWorkForm(results);
        return works;
    }

    /*
     * 月の増減処理
     */
    public void changeMonth(int months) {
        displayMonth = displayMonth.plusMonths(months);
    }

    /*
     * 月を表示するため取得処理
     */
    public int getDisplayMonth() {
        return displayMonth.getMonthValue();
    }

    private List<WorkForm> setWorkForm(List<Work> results) {
        List<WorkForm> works = new ArrayList<>();
        for (Work work : results) {
            WorkForm workForm = new WorkForm();
            workForm.setId(work.getId());
            workForm.setWorkStart(work.getWorkStart());
            workForm.setWorkEnd(work.getWorkEnd());
            workForm.setRest(work.getRest());
            workForm.setDate(work.getDate());
            workForm.setMemo(work.getMemo());
            workForm.setStatus(work.getStatus());
            workForm.setGroupId(work.getGroupId());
            workForm.setAccountId(work.getAccountId());
            works.add(workForm);
        }
        return works;
    }

    public void saveWork(WorkForm workForm, AccountForm account) throws ParseException {
//　　　　変換メソッドを用いてString型をそれぞれ変換
        convertWorkForm(workForm);
//        セッションからユーザー情報取得
        workForm.setGroupId(account.getGroupId());
        workForm.setAccountId(account.getId());

        Work work = setWork(workForm);
        workRepository.save(work);
    }

    private Work setWork(WorkForm workForm) {
        Work work = new Work();
        work.setId(workForm.getId());
        work.setWorkStart(workForm.getWorkStart());
        work.setWorkEnd(workForm.getWorkEnd());
        work.setRest(workForm.getRest());
        work.setRestStart(workForm.getRestStart());
        work.setRestEnd(workForm.getRestEnd());
        work.setDate(workForm.getDate());
        work.setStatus(workForm.getStatus());
        work.setGroupId(workForm.getGroupId());
        work.setAccountId(workForm.getAccountId());
        work.setUpdatedDate(workForm.getUpdatedDate());
        work.setMemo(workForm.getMemo());
        return work;
    }

    public WorkForm findWork(int id) {
        Work result = workRepository.findById(id).orElse(null);
        WorkForm workForm = setForm(result);
        setStr(workForm);
        return workForm;
    }

    private void setStr(WorkForm workForm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        workForm.setStrDate(sdf.format(workForm.getDate()));
        workForm.setStrWorkStart(String.valueOf(workForm.getWorkStart()).substring(0,5));
        workForm.setStrWorkEnd(String.valueOf(workForm.getWorkEnd()).substring(0,5));
        if (workForm.getRestStart() != null) {
            workForm.setStrRestStart(String.valueOf(workForm.getRestStart()).substring(0, 5));
        }
        if (workForm.getRestEnd() != null) {
            workForm.setStrRestEnd(String.valueOf(workForm.getRestEnd()).substring(0, 5));
        }
    }

    private WorkForm setForm(Work result) {
        WorkForm workForm = new WorkForm();
        workForm.setId(result.getId());
        workForm.setWorkStart(result.getWorkStart());
        workForm.setWorkEnd(result.getWorkEnd());
        workForm.setRest(result.getRest());
        workForm.setRestStart(result.getRestStart());
        workForm.setRestEnd(result.getRestEnd());
        workForm.setDate(result.getDate());
        workForm.setMemo(result.getMemo());
        workForm.setStatus(result.getStatus());
        workForm.setGroupId(result.getGroupId());
        workForm.setAccountId(result.getAccountId());
        return workForm;
    }

    private void convertWorkForm(WorkForm workForm) throws ParseException {
        //        StringからTimeに変換
        workForm.setWorkStart(Time.valueOf(workForm.getStrWorkStart() + ":00"));
        workForm.setWorkEnd(Time.valueOf(workForm.getStrWorkEnd() + ":00"));
        if (!isBlank(workForm.getStrRestStart())) {
            workForm.setRestStart(Time.valueOf(workForm.getStrRestStart() + ":00"));
        }
        if (!isBlank(workForm.getStrRestEnd())) {
            workForm.setRestEnd(Time.valueOf(workForm.getStrRestEnd() + ":00"));
        }
//        休憩時間の算出
        if (!isBlank(workForm.getStrRestStart()) && !isBlank(workForm.getStrRestEnd())){
            LocalTime startTime = LocalTime.parse(workForm.getStrRestStart(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endTime = LocalTime.parse(workForm.getStrRestEnd(), DateTimeFormatter.ofPattern("HH:mm"));
            Duration duration = Duration.between(startTime, endTime);
            workForm.setRest(Time.valueOf(duration.toHoursPart() + ":" + duration.toMinutesPart() + ":00"));
        } else {
            workForm.setRest(Time.valueOf("00:00:00"));
        }
//        日付のDate変換
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        workForm.setDate(sdf.parse(workForm.getStrDate()));
    }

    public void editWork(WorkForm workForm) throws ParseException {
        convertWorkForm(workForm);
//        更新日時取得
        workForm.setUpdatedDate(new Date());
        Work work = setWork(workForm);
        workRepository.save(work);
    }
}
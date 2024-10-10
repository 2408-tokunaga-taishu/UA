package com.example.UA.service;

import com.example.UA.controller.form.WorkForm;
import com.example.UA.repository.WorkRepository;
import com.example.UA.repository.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
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
 
    public void saveWork(WorkForm workForm) {
        Work work = setWork(workForm);
    }

    private Work setWork(WorkForm workForm) {
        Work work = new Work();
        work.setWorkStart(workForm.getWorkStart());
        work.setWorkEnd(workForm.getWorkEnd());
        return work;
    }
}
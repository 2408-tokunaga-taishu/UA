package com.example.UA.service;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.WorkForm;
import com.example.UA.repository.WorkRepository;
import com.example.UA.repository.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class WorkService {

    @Autowired
    WorkRepository workRepository;



    public void saveWork(WorkForm workForm, AccountForm account) throws ParseException {
//        StringからTimeに変換
        workForm.setWorkStart(Time.valueOf(workForm.getStrWorkStart() + ":00"));
        workForm.setWorkEnd(Time.valueOf(workForm.getStrWorkEnd() + ":00"));
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
//        セッションからユーザー情報取得
        workForm.setGroupId(account.getGroupId());
        workForm.setAccountId(account.getId());

        Work work = setWork(workForm);
        workRepository.save(work);
    }

    private Work setWork(WorkForm workForm) {
        Work work = new Work();
        work.setWorkStart(workForm.getWorkStart());
        work.setWorkEnd(workForm.getWorkEnd());
        work.setRest(workForm.getRest());
        work.setDate(workForm.getDate());
        work.setGroupId(workForm.getGroupId());
        work.setAccountId(workForm.getAccountId());
        work.setMemo(workForm.getMemo());
        return work;
    }
}

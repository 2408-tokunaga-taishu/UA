package com.example.UA.controller.form;


import com.example.UA.Validation.CheckBlank;
import com.example.UA.repository.entity.Account;
import com.example.UA.repository.entity.WorkLog;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Getter
@Setter
public class WorkForm {

    private int id;

    private Time workStart;

    private Time workEnd;

    private Time rest;

    private Time restStart;

    private Time restEnd;

    private Date date;

    @CheckBlank(message = "日付を入力してください")
    private String strDate;

    @CheckBlank(message = "勤務開始時間を入力してください")
    private String strWorkStart;

    @CheckBlank(message = "勤務終了時間を入力してください")
    private String strWorkEnd;

    @AssertTrue(message = "勤務終了時間が勤務開始時間より前に設定されています")
    public boolean isPastWorkValid() {
        if (isBlank(strWorkStart) || isBlank(strWorkEnd)) {
            return true;
        } else {
        LocalTime startTime = LocalTime.parse(strWorkStart, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(strWorkEnd, DateTimeFormatter.ofPattern("HH:mm"));
        Duration duration = Duration.between(startTime, endTime);
        return duration.getSeconds() >= 0;
        }
    }

    @AssertTrue(message = "休憩終了時間が休憩開始時間より前に設定されています")
    public boolean isPastRestValid() {
        if (isBlank(strRestStart) || isBlank(strRestEnd)) {
            return true;
        } else {
            LocalTime startTime = LocalTime.parse(strRestStart, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endTime = LocalTime.parse(strRestEnd, DateTimeFormatter.ofPattern("HH:mm"));
            Duration duration = Duration.between(startTime, endTime);
            return duration.getSeconds() >= 0;
        }
    }

    @AssertTrue(message = "休憩開始時刻のみ入力されています")
    public boolean isRestStart() {
        if (!isBlank(strRestStart) && isBlank(strRestEnd)) {
            return false;
        } else {
            return true;
        }
    }

    @AssertTrue(message = "休憩終了時刻のみ入力されています")
    public boolean isRestEnd() {
        if (isBlank(strRestStart) && !isBlank(strRestEnd)) {
            return false;
        } else {
            return true;
        }
    }

    @AssertTrue(message = "休憩時間が勤務時間外に設定されています")
    public  boolean isRest() {
        if (isBlank(strRestStart) || isBlank(strRestEnd) || isBlank(strWorkStart) || isBlank(strWorkEnd)) {
            return true;
        } else {
            LocalTime restStartTime = LocalTime.parse(strRestStart, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime restEndTime = LocalTime.parse(strRestEnd, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime startTime = LocalTime.parse(strWorkStart, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endTime = LocalTime.parse(strWorkEnd, DateTimeFormatter.ofPattern("HH:mm"));
            Duration startduration = Duration.between(startTime, restStartTime);
            Duration endduration = Duration.between(restEndTime, endTime);
            if (startduration.getSeconds() >= 0) {
                if (endduration.getSeconds() >= 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private String strRestStart;

    private String strRestEnd;

    @Size(max = 100, message = "メモは100文字以内で入力してください")
    private String memo;

    private Integer status;

    private Integer groupId;

    private Integer accountId;

    private Date createdDate;

    private Date updatedDate;

    private String remandText;

    private int stamp;

    private int restStamp;

    private  List<WorkLog> workLogs = new ArrayList<>();

}

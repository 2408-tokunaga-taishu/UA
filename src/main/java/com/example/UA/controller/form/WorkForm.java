package com.example.UA.controller.form;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Getter
@Setter
public class WorkForm {

    private int id;

    private Time workStart;

    private Time workEnd;

    private Time rest;

    private Date date;

    @NotBlank(message = "日付を入力してください")
    private String strDate;

    @NotBlank(message = "勤務開始時間を入力してください")
    private String strWorkStart;

    @NotBlank(message = "勤務終了時間を入力してください")
    private String strWorkEnd;

    @AssertTrue(message = "勤務終了時間が勤務開始時間より前に設定されています")
    public boolean isPastWorkValid() {
        if (isBlank(strWorkStart) || isBlank(strWorkEnd)) {
            return true;
        } else {
        LocalTime startTime = LocalTime.parse(strWorkStart, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(strWorkEnd, DateTimeFormatter.ofPattern("HH:mm"));
        Duration duration = Duration.between(startTime, endTime);
        return duration.getSeconds() > 0;
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
            return duration.getSeconds() > 0;
        }
    }

    private String strRestStart;

    private String strRestEnd;

    private String memo;

    private Integer status;

    private Integer groupId;

    private Integer accountId;

    private Date createdDate;

    private Date updatedDate;
}

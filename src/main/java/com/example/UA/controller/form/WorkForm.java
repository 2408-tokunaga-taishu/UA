package com.example.UA.controller.form;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


import java.sql.Time;
import java.util.Date;

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


    private String strRestStart;


    private String strRestEnd;

    private String memo;

    private Integer status;

    private Integer groupId;

    private Integer accountId;

    private Date createdDate;

    private Date updatedDate;
}

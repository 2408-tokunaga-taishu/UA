package com.example.UA.controller.form;


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

    private Time restStart;

    private Time restEnd;

    private Date date;

    private String strDate;

    private String strWorkStart;

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

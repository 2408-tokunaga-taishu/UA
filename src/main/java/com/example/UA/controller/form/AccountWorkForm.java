package com.example.UA.controller.form;


import com.example.UA.Validation.CheckBlank;
import com.example.UA.repository.entity.Account;
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
public class AccountWorkForm {

    private int id;

    private Time workStart;

    private Time workEnd;

    private Time rest;

    private Time restStart;

    private Time restEnd;

    private Date date;

    private String strRestStart;

    private String strRestEnd;

    private String memo;

    private Integer status;

    private Integer groupId;

    private Integer accountId;

    private Date createdDate;

    private Date updatedDate;

    private String accountName;
}

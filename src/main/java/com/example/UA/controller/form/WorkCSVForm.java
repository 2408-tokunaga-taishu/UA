package com.example.UA.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class WorkCSVForm {

    private LocalTime workStart;

    private LocalTime workEnd;

    private LocalTime rest;

    private Date date;
}

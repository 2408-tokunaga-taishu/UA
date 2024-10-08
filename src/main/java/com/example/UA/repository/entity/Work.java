package com.example.UA.repository.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "works")
@Getter
@Setter
public class Work {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "work_start")
    private Time workStart;

    @Column(name = "work_end")
    private Time workEnd;

    @Column(name = "rest")
    private Time rest;

    @Column(name = "date")
    private Date date;

    @Column(name = "memo")
    private String memo;

    @Column(name = "status")
    private Integer status;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name="created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Column(name="updated_date", insertable = false)
    private Date updatedDate;


}

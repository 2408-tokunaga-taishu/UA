package com.example.UA.repository.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "rest_start")
    private Time restStart;

    @Column(name = "rest_end")
    private Time restEnd;

    @Column(name = "date")
    private Date date;

    @Column(name = "memo")
    private String memo;

    @Column(name = "status", insertable = false)
    private Integer status;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name="created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Column(name="updated_date", insertable = false)
    private Date updatedDate;

    @Column(name = "remand_text", insertable = false, updatable = false)
    private String remandText;

    @Column(name = "stamping", insertable = false, updatable = false)
    private int stamp;

    @Column(name = "rest_stamping", insertable = false, updatable = false)
    private int restStamp;

    @ManyToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @OneToMany
    @JoinColumn(name = "work_id" , insertable = false, updatable = false)
    private List<WorkLog> workLogs = new ArrayList<>();
}

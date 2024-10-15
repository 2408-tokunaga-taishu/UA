package com.example.UA.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "is_stopped")
    private Integer isStopped;

    @Column(name = "supervisor")
    private Integer superVisor;

    @Column(name = "admin")
    private Integer admin;

    @Column(name="created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Column(name="updated_date", insertable = false)
    private Date updatedDate;
    
}

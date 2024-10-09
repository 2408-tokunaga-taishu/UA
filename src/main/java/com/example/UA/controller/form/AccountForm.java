package com.example.UA.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountForm {

    private int id;

    private String account;

    private String password;

    private String name;

    private Integer groupId;

    private Integer isStopped;

    private Integer superVisor;

    private Integer admin;

    private Date createdDate;

    private Date updatedDate;
}

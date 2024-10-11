package com.example.UA.controller.form;

import com.example.UA.Validation.CheckBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountForm {

    private Integer id;

    @CheckBlank(message = "アカウントを入力してください")
    private String account;

    @CheckBlank(message = "パスワードを入力してください")
    private String password;

    private String name;

    private Integer groupId;

    private Integer isStopped;

    private Integer superVisor;

    private Integer admin;

    private Date createdDate;

    private Date updatedDate;

    private String groupName;
}

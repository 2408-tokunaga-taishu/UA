package com.example.UA.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GroupForm {
    private Integer id;
    private String name;
    private Date createdDate;
    private Date updatedDate;
}

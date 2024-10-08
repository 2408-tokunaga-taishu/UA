package com.example.UA.controller;

import com.example.UA.controller.form.WorkForm;
import com.example.UA.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WorkController {

    @Autowired
    WorkService workService;

//    新規追加初期画面表示
    @GetMapping("/newWork")
    public ModelAndView newWork() {
        WorkForm workForm = new WorkForm();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/newWork");
        mav.addObject("workForm", workForm);
        return mav;
    }

//    新規追加処理
    @PostMapping("/addWork")
    public ModelAndView addWork(@ModelAttribute("workForm") WorkForm workForm) {
        ModelAndView mav = new ModelAndView();
        workService.saveWork(workForm);
        mav.setViewName("redirect:/");
        return mav;
    }
}

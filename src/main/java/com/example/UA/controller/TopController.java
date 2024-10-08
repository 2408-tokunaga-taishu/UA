package com.example.UA.controller;

import com.example.UA.controller.form.WorkForm;
import com.example.UA.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TopController {
    @Autowired
    WorkService workService;
    /*
     * ホーム画面表示
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        List<WorkForm> works = workService.findAllWorks();
        mav.addObject("works", works);
        mav.setViewName("/top");
        return mav;
    }

}

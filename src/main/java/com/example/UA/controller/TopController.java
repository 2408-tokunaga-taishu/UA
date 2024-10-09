package com.example.UA.controller;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.WorkForm;
import com.example.UA.service.WorkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TopController {
    @Autowired
    WorkService workService;

    @Autowired
    private HttpSession session;
    /*
     * ホーム画面表示
     */
    @GetMapping({"/top", "/"})
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        List<WorkForm> works = workService.findAllWorks();
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        mav.addObject("works", works);
        mav.addObject("loginAccount", loginAccount);
        mav.setViewName("/top");
        return mav;
    }

}

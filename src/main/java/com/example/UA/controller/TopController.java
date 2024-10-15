package com.example.UA.controller;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.AccountWorkForm;
import com.example.UA.controller.form.WorkForm;
import com.example.UA.repository.entity.Work;
import com.example.UA.service.WorkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
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
    public ModelAndView top() throws ParseException {
        ModelAndView mav = new ModelAndView();
        List<WorkForm> works = workService.findAllWorks();
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        //　アカウント管理画面表示フラグ
        boolean isShowAccountManage = false;
        if (loginAccount.getAdmin() == 1) {
            isShowAccountManage = true;
        }
//      承認待ち勤怠数の取得
        if (loginAccount.getSuperVisor() == 1) {
        int count = workService.findGroupWorkCount(loginAccount.getGroupId());
        mav.addObject("count", count);
        }
//      差し戻し勤怠数の取得
        int remand = workService.findRemandWorkCount(loginAccount.getId());
        mav.addObject("remand", remand);
        mav.addObject("works", works);
        mav.addObject("displayMonth", workService.getDisplayMonth());
        mav.addObject("loginAccount", loginAccount);
        mav.addObject("isShowAccountManage", isShowAccountManage);
        mav.setViewName("/top");
        return mav;
    }

    /*
     * 前月のデータ表示(日付絞り込み)
     */
    @PostMapping("/previousMonth")
    public ModelAndView previousMonth() throws ParseException {
        ModelAndView mav = new ModelAndView();
        workService.changeMonth(-1);
        mav.setViewName("redirect:/top");
        return mav;
    }

    /*
     * 次月のデータ表示(日付絞り込み)
     */
    @PostMapping("/nextMonth")
    public ModelAndView nextMonth() {
        ModelAndView mav = new ModelAndView();
        workService.changeMonth(1);
        mav.setViewName("redirect:/top");
        return mav;
    }

    @GetMapping("/approval")
    public ModelAndView approval() {
        ModelAndView mav = new ModelAndView();
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        List<AccountWorkForm> accountWorkForms = workService.findGroupWork(loginAccount.getGroupId());
        mav.setViewName("/approval");
        mav.addObject("accountWorkForms",accountWorkForms);
        return mav;
    }
}

package com.example.UA.controller;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.WorkForm;
import com.example.UA.service.WorkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkController {

    @Autowired
    WorkService workService;

    @Autowired
    HttpSession session;

//    新規追加初期画面表示
    @GetMapping("/newWork")
    public ModelAndView newWork(@ModelAttribute("workForm") WorkForm workForm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/newWork");
        mav.addObject("workForm", workForm);
        return mav;
    }

//    新規追加処理
    @PostMapping("/addWork")
    public ModelAndView addWork(@ModelAttribute("workForm") @Validated WorkForm workForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws ParseException {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        if (errorMessages.size() > 0) {
            redirectAttributes.addFlashAttribute("errorMessages",errorMessages);
            redirectAttributes.addFlashAttribute("workForm", workForm);
            mav.setViewName("redirect:/newWork");
        } else {
            AccountForm account = (AccountForm) session.getAttribute("loginAccount");
            workService.saveWork(workForm, account);
            mav.setViewName("redirect:/");
        }
        return mav;
    }

//    勤怠編集初期表示
    @GetMapping("/editWork/{id}")
    public ModelAndView edit(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        WorkForm work = workService.findWork(id);
        mav.setViewName("/editWork");
        mav.addObject("work", work);
        return mav;
    }

//    勤怠修正処理
    @PutMapping("/editWork/{id}")
    public ModelAndView putWork (@ModelAttribute ("work") @Validated WorkForm workForm, BindingResult bindingResult) throws ParseException {
        List<String> errorMessages = new ArrayList<>();
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        if (errorMessages.size() > 0) {
            mav.addObject("errorMessages",errorMessages);
            mav.addObject("work", workForm);
            mav.setViewName("/editWork");
        } else {
            workService.editWork(workForm);
            mav.setViewName("redirect:/");
        }
        return mav;
    }

//    勤怠の申請処理
    @PutMapping("/request/{id}")
    public ModelAndView requestWork(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        workService.saveStatus(id);
        mav.setViewName("redirect:/");
        return mav;
    }

//    勤怠の削除処理
    @DeleteMapping("/deleteWork/{id}")
    public ModelAndView deleteWork(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        workService.deleteWork(id);
        mav.setViewName("redirect:/");
        return mav;
    }

//    勤怠の承認
    @PutMapping("/approval/{id}")
    public ModelAndView approval (@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        workService.approval(id);
        mav.setViewName("redirect:/approval");
        return mav;
    }
//    勤怠の差し戻し
    @PutMapping("/remand/{id}")
    public ModelAndView remand(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        workService.remand(id);
        mav.setViewName("redirect:/approval");
        return mav;
    }
}

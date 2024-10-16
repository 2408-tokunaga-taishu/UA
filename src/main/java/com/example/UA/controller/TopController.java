package com.example.UA.controller;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.AccountWorkForm;
import com.example.UA.controller.form.WorkForm;
import com.example.UA.service.AccountService;
import com.example.UA.service.WorkService;
import com.example.UA.utils.CipherUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TopController {
    @Autowired
    WorkService workService;

    @Autowired
    AccountService accountService;

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
        // 当月の出勤日数算出(streamAPI)
        long countDays = works.stream()
                .filter(work -> work.getAccountId().equals(loginAccount.getId()))
                .count();
        // 当月の労働時間算出
        String totalWorkingTime = workService.calculateWorkingTime(works);
        mav.addObject("totalWorkingTime", totalWorkingTime);
//      承認待ち勤怠数の取得
        if (loginAccount.getSuperVisor() == 1) {
        int workCount = workService.findGroupWorkCount(loginAccount.getGroupId());
        mav.addObject("count", workCount);
        }
//      差し戻し勤怠数の取得
        int remand = workService.findRemandWorkCount(loginAccount.getId());
        mav.addObject("remand", remand);
//        エラーメッセージをSessionから取得
        mav.addObject("errorMessages", session.getAttribute("errorMessages"));

        mav.addObject("works", works);
        mav.addObject("displayMonth", workService.getDisplayMonth());
        mav.addObject("loginAccount", loginAccount);
        mav.addObject("isShowAccountManage", isShowAccountManage);
        mav.addObject("countDays", countDays);
        mav.setViewName("/top");
        session.removeAttribute("errorMessages");
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

    /*
     * パスワード変更画面表示
     */
    @GetMapping("/settingPassword/{id}")
    public ModelAndView settingPassword (@PathVariable String id, RedirectAttributes redirectAttributes) {
        // URLの数字チェック
        if (!id.matches("^[0-9]*$")) {
            redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
            return new ModelAndView("redirect:/top");
        }
        ModelAndView mav = new ModelAndView();
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        try {
            AccountForm account = accountService.findAccount(Integer.parseInt(id));
            mav.addObject("accountForm", account);
            // 別の人のアカウント変更ページを開いたらエラーを投げる
            if (account.getId() != loginAccount.getId()) {
                throw new Exception();
            }
            mav.setViewName("/settingPassword");
            return mav;
        } catch (Exception e) {
            // idが存在しない値、もしくはログインしているアカウント以外の存在するアカウントのページを開いた場合
            redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
            return new ModelAndView("redirect:/top");
        }
    }
    /*
     * パスワード編集画面でパラメータに数字が無かったとき用の処理
     */
    @GetMapping({"/settingPassword", "/settingPassword/"})
    public ModelAndView noIdSettingPassword (RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
        return new ModelAndView("redirect:/top");
    }

    /*
     * パスワード変更処理
     */
    @PutMapping("/settingPassword/{id}")
    public ModelAndView settingPassword(@PathVariable int id, @Validated({AccountForm.settingPassword.class}) AccountForm accountForm,
                                        BindingResult result) {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();
        // エラー処理
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        if (errorMessages.size() > 0) {
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("accountForm", accountForm);
            AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
            mav.addObject("loginAccount", loginAccount);
            mav.setViewName("/settingPassword");

        }
        if (errorMessages.isEmpty()) {
            AccountForm editAccount = accountService.findAccount(id);
            // パスワード暗号化
            editAccount.setPassword(CipherUtil.encrypt(accountForm.getPassword()));
            accountService.saveAccount(editAccount);
            mav.setViewName("redirect:/top");

        }
        return mav;
    }

    @GetMapping("/approval")
    public ModelAndView approval() throws ParseException {
        ModelAndView mav = new ModelAndView();
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        Map<String, List<AccountWorkForm>> accountWorkForms = workService.findGroupWork(loginAccount.getGroupId());
        mav.setViewName("/approval");
        mav.addObject("accountWorkForms",accountWorkForms);
        return mav;
    }
}

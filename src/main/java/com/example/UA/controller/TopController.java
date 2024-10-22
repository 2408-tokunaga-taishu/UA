package com.example.UA.controller;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.AccountWorkForm;
import com.example.UA.controller.form.WorkCSVForm;
import com.example.UA.controller.form.WorkForm;
import com.example.UA.service.AccountService;
import com.example.UA.service.WorkService;
import com.example.UA.utils.CipherUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @GetMapping({"/"})
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
        List<WorkForm> personalWorks = workService.findWorksByAccountId(loginAccount.getId());
        String totalWorkingTime = workService.calculateWorkingTime(personalWorks);
        mav.addObject("totalWorkingTime", totalWorkingTime);
        // 当月の休憩時間
        String totalRestTime = workService.calculateRestTime(personalWorks);
        mav.addObject("totalRestTime", totalRestTime);
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
        mav.addObject("displayYear", workService.getDisplayYear());
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
        mav.setViewName("redirect:/");
        return mav;
    }

    /*
     * 次月のデータ表示(日付絞り込み)
     */
    @PostMapping("/nextMonth")
    public ModelAndView nextMonth() {
        ModelAndView mav = new ModelAndView();
        workService.changeMonth(1);
        mav.setViewName("redirect:/");
        return mav;
    }

    /*
     * パスワード変更画面表示
     */
    @GetMapping("/settingPassword/{id}")
    public ModelAndView settingPassword (@PathVariable String id, RedirectAttributes redirectAttributes) {
        // URLの数字チェック
        if (!id.matches("^[0-9]*$")) {
            redirectAttributes.addFlashAttribute("passErrorMessages", "不正なパラメータです");
            return new ModelAndView("redirect:/");
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
            redirectAttributes.addFlashAttribute("passErrorMessages", "不正なパラメータです");
            return new ModelAndView("redirect:/");
        }
    }
    /*
     * パスワード編集画面でパラメータに数字が無かったとき用の処理
     */
    @GetMapping({"/settingPassword", "/settingPassword/"})
    public ModelAndView noIdSettingPassword (RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("passErrorMessages", "不正なパラメータです");
        return new ModelAndView("redirect:/");
    }

    /*
     * パスワード変更処理
     */
    @PutMapping("/settingPassword/{id}")
    public ModelAndView settingPassword(@PathVariable int id, @Validated({AccountForm.settingPassword.class}) AccountForm accountForm,
                                        BindingResult result) {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();
        // 現在のパスワードが正しいかチェック
        String registeredPassword = accountService.findAccount(id).getPassword();
        accountForm.setOldPassword(CipherUtil.encrypt(accountForm.getOldPassword()));
        if (!registeredPassword.equals(accountForm.getOldPassword())) {
            errorMessages.add("現在のパスワードが正しくありません");
        }
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
            mav.setViewName("redirect:/");

        }
        return mav;
    }

    @GetMapping("/approval")
    public ModelAndView approval() throws ParseException {
        ModelAndView mav = new ModelAndView();
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        Map<String, List<AccountWorkForm>> accountWorkForms = workService.findGroupWork(loginAccount.getGroupId());
        //　アカウント管理画面表示フラグ
        boolean isShowAccountManage = false;
        if (loginAccount.getAdmin() == 1) {
            isShowAccountManage = true;
        }
        mav.addObject("isShowAccountManage", isShowAccountManage);
        mav.setViewName("/approval");
        mav.addObject("accountWorkForms",accountWorkForms);

        return mav;
    }

    /*
     * csvファイルDL
     */
    @PostMapping(value = "/download/csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> downloadCsv() throws IOException, ParseException {
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        int id = loginAccount.getId();
        String csvData = workService.findCSVWorksByAccountId(id);

        byte[] csvBytes = csvData.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("sample.csv").build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
}

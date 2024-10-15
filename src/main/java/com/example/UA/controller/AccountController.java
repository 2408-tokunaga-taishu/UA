package com.example.UA.controller;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.GroupForm;
import com.example.UA.service.AccountService;
import com.example.UA.utils.CipherUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private HttpSession session;

    @Autowired
    AccountService accountService;

    /*
     * ログイン画面表示
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        AccountForm accountForm = new AccountForm();
        // 遷移先
        mav.setViewName("login");
        // 空のフォームの送信
        mav.addObject("accountForm", accountForm);
        mav.addObject("errorMessage",session.getAttribute("errorMessage"));
        session.removeAttribute("errorMessage");
        return mav;
    }

    /*
     * ログイン処理
     */
    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("accountForm") @Validated({AccountForm.login.class}) AccountForm accountForm,
                              BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/login");
        }
        // パスワードを暗号化
        String encPassword = CipherUtil.encrypt(accountForm.getPassword());
        // 暗号化した物をuserFormにセット
        accountForm.setPassword(encPassword);
        // try文の中と処理後にもuserDataオブジェクトを使用できるように先に作成
        AccountForm accountData;
        try {
            accountData = accountService.selectAccount(accountForm);
            // もしユーザが停止している場合,もしくはユーザ情報が存在しない場合
            if (accountData.getIsStopped() == 1) {
                // 例外を投げてcatchで処理をする
                throw new Exception("ログインに失敗しました");
            }
        } catch (Exception e) {
            // 設定したメッセージを取得しerrorMessageとしてhtmlに渡す
            mav.addObject("errorMessage", e.getMessage());
            mav.setViewName("/login");
            return mav;
        }
        // セッションに値をセット
        session.setAttribute("loginAccount", accountData);
        // topにリダイレクト
        mav.setViewName("redirect:/top");
        return mav;
    }

    /*
     * ログアウト処理
     */
    @GetMapping("/logout")
    public ModelAndView logout() {
        ModelAndView mav = new ModelAndView();
        session.removeAttribute("loginAccount");
        AccountForm accountForm = new AccountForm();
        mav.setViewName("/login");
        mav.addObject("accountForm", accountForm);
        return mav;
    }

    /*
     * アカウント管理画面表示
     */
    @GetMapping("/accountManage")
    public ModelAndView accountManage() {
        ModelAndView mav = new ModelAndView();
        List<AccountForm> accounts = accountService.findAllAccount();
        mav.setViewName("/accountManage");
        mav.addObject("accounts", accounts);
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        mav.addObject("loginAccount", loginAccount);
        return mav;
    }

    /*
     * アカウント復活・停止機能
     */
    @PutMapping("/{id}")
    public ModelAndView changeIsStopped(@PathVariable("id") int id) {
        // 対象のアカウントを取得
        AccountForm accountData = accountService.findAccount(id);
        // アカウントのisStoppedの値を変更させる
        if (accountData.getIsStopped() == 1) {
            accountData.setIsStopped(0);
        } else {
            accountData.setIsStopped(1);
        }
        // 値を変更させたらデータを保存させる
        accountService.saveAccount(accountData);
        return new ModelAndView("redirect:/accountManage");
    }

    /*
     * アカウント登録画面表示
     */
    @GetMapping("/newAccount")
    public ModelAndView newAccount(@ModelAttribute("accountForm") AccountForm accountForm) {
        ModelAndView mav = new ModelAndView();
        // selectタグで表示するグループ名を取得
        List<GroupForm> groups = accountService.findAllGroups();
        mav.addObject("groups", groups);
        mav.setViewName("/newAccount");
        return mav;
    }

    /*
     * アカウント登録処理
     */
    @PostMapping("/newAccount")
    public ModelAndView newAccount(@ModelAttribute("accountForm") @Validated({AccountForm.newAccount.class}) AccountForm accountForm,
                                   BindingResult result) {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();
        // 0-1の切り替えフラグのカラムに値を追加
        accountForm.setIsStopped(0);
        accountForm.setSuperVisor(0);
        accountForm.setAdmin(0);
        // エラー処理
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        if (errorMessages.size() > 0) {
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("accountForm", accountForm);
            mav.addObject("groups", accountService.findAllGroups());
            mav.setViewName("/newAccount");
        } else {
            // パスワード暗号化
            accountForm.setPassword(CipherUtil.encrypt(accountForm.getPassword()));
            accountService.saveAccount(accountForm);
            mav.setViewName("redirect:/accountManage");
        }
        return mav;
    }

    /*
     * アカウント編集画面表示
     */
    @GetMapping("/editAccount/{id}")
    public ModelAndView editAccount (@PathVariable String id, RedirectAttributes redirectAttributes) {
        // URLの数字チェック
        if (!id.matches("^[0-9]*$")) {
            redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
            return new ModelAndView("redirect:/accountManage");
        }
        ModelAndView mav = new ModelAndView();
        try {
            AccountForm account = accountService.findAccount(Integer.parseInt(id));
            mav.addObject("accountForm", account);
            // グループ情報をDBから持ってきたいので取得。
            List<GroupForm> groups = accountService.findAllGroups();
            mav.addObject("groups", groups);
            AccountForm loginAccount = (AccountForm) session.getAttribute("loginAccount");
            mav.addObject("loginAccount", loginAccount);
            mav.setViewName("/editAccount");
            return mav;
        } catch (Exception e) {
            //idが存在しない値だった場合
            redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
            return new ModelAndView("redirect:/accountManage");
        }
    }

    /*
     * 編集画面表示(idがURLにのってなかった場合のバリデーションの役割)
     */
    @GetMapping({"/editAccount", "/editAccount/"})
    public ModelAndView noIdEditAccount (RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
        return new ModelAndView("redirect:/accountManage");
    }

    /*
     * アカウント編集処理
     */
    @PutMapping("/editAccount/{id}")
    public ModelAndView editAccount(@PathVariable int id, @Validated({AccountForm.editAccount.class}) AccountForm accountForm,
                                 BindingResult result) {
        ModelAndView mav = new ModelAndView();
        List<String> errorMessages = new ArrayList<>();
        // エラー処理
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        if (errorMessages.isEmpty()) {
            try {
                accountService.updateAccount(accountForm);
                mav.setViewName("redirect:/accountManage");
                return mav;
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
            }
        }
        if (errorMessages.size() > 0) {
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("accountForm", accountForm);
            // グループ名が選択肢からなくなってしまうのでmavにaddする
            mav.addObject("groups", accountService.findAllGroups());
            AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
            mav.addObject("loginAccount", loginAccount);
            mav.setViewName("/editAccount");
            return mav;
        }
        return mav;
    }
}

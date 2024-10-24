package com.example.UA.controller;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.GroupForm;
import com.example.UA.repository.entity.Account;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccountController {
    @Autowired
    private HttpSession session;

    @Autowired
    AccountService accountService;

    // 1ページの表示数
    private final String limit = "3";

    // ページネーションで表示するページ数
    private int showPageSize = 3;

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
        List<String> errorMessages = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        if (errorMessages.size() > 0) {
            mav.addObject("errorMessages", errorMessages);
            mav.addObject("accountForm", accountForm);
            mav.setViewName("/login");
        } else {
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
                errorMessages.add(e.getMessage());
                mav.addObject("errorMessages", errorMessages);
                mav.setViewName("/login");
                return mav;
            }
            // セッションに値をセット
            session.setAttribute("loginAccount", accountData);
            // topにリダイレクト
            mav.setViewName("redirect:/");
            return mav;
        }
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
        mav.setViewName("redirect:/login");
        mav.addObject("accountForm", accountForm);
        return mav;
    }

    /*
     * アカウント管理画面表示
     */
    @GetMapping("/accountManage")
    public ModelAndView accountManage(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        // paramsのvalueの値チェック
        for (String value : params.values()) {
            if(value.isBlank() || !value.matches("^[0-9]*$")) {
                redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
                return new ModelAndView("redirect:/accountManage");
            }
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/accountManage");
        AccountForm loginAccount = (AccountForm)session.getAttribute("loginAccount");
        mav.addObject("loginAccount", loginAccount);

        String currentPage = params.get("page");
        // 最初のページだけcurrentPageの値がnullになるので値をセットしてあげる
        if (currentPage == null) {
            currentPage = "1";
        }
        HashMap<String, String> search = new HashMap<>(); //Mapだと抽象クラスなのでインスタンス化不可
        search.put("limit", limit);
        search.put("page", currentPage);

        List<AccountForm> showAccounts = accountService.findAllAccountByLimit(Integer.parseInt(limit), Integer.parseInt(currentPage));
        List<AccountForm> accounts = accountService.findAllAccount();
        int total = accounts.size();
        // ページネーション処理
        // 総数/1ページの表示数 から総ページ数を割り出す
        int totalPage = (total + Integer.parseInt(limit) -1)/ Integer.parseInt(limit);
        // パラメータに入力された数値が総ページ数よりも大きい場合のエラー処理
        for (String value : params.values()) {
            if(Integer.parseInt(value) > totalPage) {
                redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
                return new ModelAndView("redirect:/accountManage");
            }
        }
        int page = Integer.parseInt(currentPage);
        // 表示する最初のページ番号を算出
        int startPage = page - (page - 1) % showPageSize;
        // 表示する最後のページ番号を算出
        int endPage = (startPage + showPageSize -1 > totalPage) ? totalPage : startPage + showPageSize -1;
        mav.addObject("total", total);
        mav.addObject("page", page);
        mav.addObject("totalPage", totalPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("showAccounts", showAccounts);
        mav.addObject("accounts", accounts);
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

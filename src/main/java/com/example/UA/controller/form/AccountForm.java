package com.example.UA.controller.form;

import com.example.UA.Validation.CheckBlank;
import com.example.UA.Validation.Unique;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class AccountForm {

    // 適応させるバリデーションをグループで分ける
    public static interface login{}
    public static interface newAccount{}
    public static interface editAccount{}
    public static interface settingPassword{}

    private int id;

    @CheckBlank(message = "アカウント名を入力してください", groups = {login.class, newAccount.class, editAccount.class})
    @Unique(groups = {newAccount.class})
    private String account;

    @AssertTrue(message = "アカウント名は半角文字かつ6文字以上20文字以下で入力してください", groups = {newAccount.class, editAccount.class})
    private boolean isAccountValid() {
        if (account.isBlank()) {
            return true; //アカウント名が空の場合は@CheckBlankでまずバリデーションするため処理を抜ける
        } else {
            // 以下の条件を満たしていない場合はエラーメッセージを表示する
            return (account.length() >= 6 && account.length() <= 20 && account.matches("^[\\x20-\\x7E]+$"));
        }
    }

    @CheckBlank(message = "パスワードを入力してください", groups = {login.class, newAccount.class})
    private String password;

    @CheckBlank(message = "パスワード(確認用)を入力してください", groups = {newAccount.class})
    private String passCheck;

    private String oldPassword;

    @CheckBlank(message = "新しいパスワードを入力してください", groups = {settingPassword.class})
    private String newPassword;

    @CheckBlank(message = "新しいパスワード(確認用)を入力してください", groups = {settingPassword.class})
    private String newPassCheck;

    @AssertTrue(message = "パスワードは半角文字かつ6文字以上20文字以下で入力してください", groups = {newAccount.class})
    private boolean isPasswordValid() {
        if (password.isBlank()) {
            return true; //パスワードが空の場合は@CheckBlankでまずバリデーションするため処理を抜ける
        } else {
            // 以下の条件を満たしていない場合はエラーメッセージを表示する
            return (password.length() >= 6 && password.length() <= 20 && password.matches("^[\\x20-\\x7E]+$"));
        }
    }

    @AssertTrue(message = "新しいパスワードは半角文字かつ6文字以上20文字以下で入力してください", groups = {settingPassword.class})
    private boolean isNewPasswordValid() {
        if (newPassword.isBlank()) {
            return true; //パスワードが空の場合は@CheckBlankでまずバリデーションするため処理を抜ける
        } else {
            // 以下の条件を満たしていない場合はエラーメッセージを表示する
            return (newPassword.length() >= 6 && newPassword.length() <= 20 && newPassword.matches("^[\\x20-\\x7E]+$"));
        }
    }

    @AssertTrue(message = "パスワードと確認用パスワードが一致しません", groups = {newAccount.class})
    private boolean isSamePassword() {
        if (password.isBlank()) {
            return true;
        } else {
            return Objects.equals(password, passCheck);
        }
    }

    @AssertTrue(message = "新しいパスワードと新しいパスワード(確認用)が一致しません", groups = {settingPassword.class})
    private boolean isSameNewPassword() {
        if (newPassCheck.isBlank()) {
            return true;
        } else {
            return Objects.equals(newPassword, newPassCheck);
        }
    }


    @CheckBlank(message = "氏名を入力してください", groups = {newAccount.class, editAccount.class})
    private String name;

    @AssertTrue(message = "氏名は10文字以下で入力してください", groups = {newAccount.class, editAccount.class})
    private boolean isNameValid() {
        if (name.isBlank()) {
            return true;
        } else {
            return (name.length() <= 10);
        }
    }

    @NotNull(message = "所属グループを選択してください", groups = {newAccount.class, editAccount.class})
    private Integer groupId;

    private Integer isStopped;

    private Integer superVisor;

    private Integer admin;

    private Date createdDate;

    private Date updatedDate;

    private String groupName;
}

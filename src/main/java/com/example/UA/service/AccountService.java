package com.example.UA.service;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.repository.AccountRepository;
import com.example.UA.repository.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    /*
     * ログインアカウント情報取得(ログイン処理)
     */
    public AccountForm selectAccount(AccountForm accountForm) throws Exception {
        String accountName = accountForm.getAccount(); //ログインアカウントのaccountカラムのこと
        String password = accountForm.getPassword();
        Account result = accountRepository.findByAccountAndPassword(accountName, password);
        if (result == null) {
            // Controllerにエラーを投げる
            throw new Exception("ログインに失敗しました");
        }
        AccountForm account = setAccountForm(result);
        return account;
    }
    
    private AccountForm setAccountForm(Account result) {
        AccountForm account = new AccountForm();
        account.setId(result.getId());
        account.setPassword(result.getPassword());
        account.setAccount(result.getAccount());
        account.setName(result.getName());
        account.setGroupId(result.getGroupId());
        account.setIsStopped(result.getIsStopped());
        account.setSuperVisor(result.getSuperVisor());
        account.setAdmin(result.getAdmin());
        return account;
    }
}

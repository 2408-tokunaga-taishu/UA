package com.example.UA.service;

import com.example.UA.controller.form.AccountForm;
import com.example.UA.controller.form.GroupForm;
import com.example.UA.repository.AccountRepository;
import com.example.UA.repository.GroupRepository;
import com.example.UA.repository.entity.Account;
import com.example.UA.repository.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GroupRepository groupRepository;

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

    /*
     * アカウント管理画面で表示する全アカウント取得
     */
    public List<AccountForm> findAllAccount() {
        List<Account> results = accountRepository.findAllByOrderById();
        List<AccountForm> accounts = setAccountForm(results);
        return accounts;
    }

    private List<AccountForm> setAccountForm(List<Account> results) {
        List<AccountForm> accounts = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            AccountForm account = new AccountForm();
            Account result = results.get(i);
            account.setId(result.getId());
            account.setAccount(result.getAccount());
            account.setName(result.getName());
            account.setGroupId(result.getGroupId());
            account.setIsStopped(result.getIsStopped());
            account.setSuperVisor(result.getSuperVisor());
            account.setAdmin(result.getAdmin());
            account.setGroupName(result.getGroup().getName());
            accounts.add(account);
        }
        return accounts;
    }

    /*
     * 稼働状況を更新するアカウントを取得
     */
    public AccountForm findAccount(int id) {
        Account result = accountRepository.findById(id).orElse(null);
        AccountForm account = setAccountForm(result);
        return account;
    }

    /*
     * アカウントの登録処理
     */
    public void saveAccount(AccountForm accountForm) {
        Account saveAccount = setAccountEntity(accountForm);
        accountRepository.save(saveAccount);
    }

    /*
     * アカウントの更新処理
     */
    public void updateAccount(AccountForm accountForm) throws Exception {
        // アカウント名重複チェック
        Account account = accountRepository.findByAccount(accountForm.getAccount());
        if (account != null) {
            if (account.getId() != accountForm.getId()) {
                throw new Exception("アカウントが重複しています");
            }
        }
        Account updateAccount = setAccountEntity(accountForm);
        accountRepository.save(updateAccount);
    }

    private Account setAccountEntity(AccountForm accountForm) {
        Account account = new Account();
        account.setId(accountForm.getId());
        account.setPassword(accountForm.getPassword());
        account.setAccount(accountForm.getAccount());
        account.setName(accountForm.getName());
        account.setGroupId(accountForm.getGroupId());
        account.setIsStopped(accountForm.getIsStopped());
        account.setSuperVisor(accountForm.getSuperVisor());
        account.setAdmin(accountForm.getAdmin());
        account.setUpdatedDate(new Date());
        return account;
    }

    /*
     * アカウント登録と編集時に現在あるグループを取得
     */
    public List<GroupForm> findAllGroups() {
        List<Group> results = groupRepository.findAll();
        List<GroupForm> groups = setGroupForm(results);
        return groups;
    }

    private List<GroupForm> setGroupForm(List<Group> results) {
        List<GroupForm> groups = new ArrayList<>();
        for (int i = 0; i <results.size(); i++) {
            GroupForm group = new GroupForm();
            Group result = results.get(i);
            group.setId(result.getId());
            group.setName(result.getName());
            groups.add(group);
        }
        return groups;
    }
}
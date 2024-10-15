package com.example.UA.repository;

import com.example.UA.repository.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountAndPassword(String accountName, String password);

    Account findByAccount(String account);

    List<Account> findAllByOrderById();
}

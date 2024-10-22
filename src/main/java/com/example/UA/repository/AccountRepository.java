package com.example.UA.repository;

import com.example.UA.repository.entity.Account;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountAndPassword(String accountName, String password);

    Account findByAccount(String account);

    @Query("SELECT a FROM Account a WHERE a.id BETWEEN :start AND :end ORDER BY a.id LIMIT :limit")
    List<Account> findAllByOrderByIdBetween(@Param("start") int start, @Param("end")int end, @Param("limit")int limit);

    List<Account> findAllByOrderById();
}

package com.smartbudget.repository;

import com.smartbudget.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAccountsByUserId(Long UserId);

    Account findAccountById(Long accountId);

    Account findAccountByIdAndUserId(Long accountId, Long UserId);

    void deleteAccountByIdAndUserId(Long accountId, Long UserId);

}

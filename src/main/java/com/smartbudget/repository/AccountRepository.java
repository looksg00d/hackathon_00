package com.smartbudget.repository;

import com.smartbudget.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAccountsByUserId(Long userId);
    Account findAccountById(Long accountId);
    Account findAccountByIdAndUserId(Long accountId, Long userId);
    void deleteAccountByIdAndUserId(Long accountID, Long userId);
}





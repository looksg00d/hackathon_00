package com.smartbudget.repository;


import com.smartbudget.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where (t.date between :periodFrom and :periodTo)" +
            " and (t.fromAccountId = :accountId or t.toAccountId = :accountId)")
    List<Transaction> findTransactionsByDateBetween(LocalDateTime periodFrom, LocalDateTime periodTo, Long accountId);
}

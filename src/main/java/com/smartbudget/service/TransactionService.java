package com.smartbudget.service;

import com.smartbudget.DTO.TransactionDTO;
import com.smartbudget.converter.Converter;
import com.smartbudget.model.Account;
import com.smartbudget.model.Transaction;
import com.smartbudget.repository.AccountRepository;
import com.smartbudget.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final Converter<Transaction, TransactionDTO> transactionDTOConverter;

    public List<TransactionDTO> moneyTransferReport(LocalDateTime periodFrom, LocalDateTime periodTo, Long userId) {

        List<Account> accounts = accountRepository.findAccountsByUserId(userId);

        return accounts.stream()
                .flatMap(account -> transactionRepository
                        .findTransactionsByDateBetween(periodFrom, periodTo, account.getId())
                        .stream()
                        .map(transactionDTOConverter::convert))
                .collect(Collectors.toList());

    }

    @Transactional
    public TransactionDTO newTransaction(String transactionName, LocalDateTime date, Long fromAccountId,
                                         Long toAccountId, int amount, Long userId) {

        if (fromAccountId == null && toAccountId == null) {
            return null;
        }

        for (Account account : accountRepository.findAccountsByUserId(userId)) {
            if (fromAccountId == null) {
                if (toAccountId.equals(account.getId())) {
                    break;
                }
            } else if (toAccountId == null) {
                if (fromAccountId.equals(account.getId())) {
                    break;
                }
            }
        }

        Transaction transaction = new Transaction();
        transaction.setName(transactionName);
        transaction.setDate(date);
        transaction.setFromAccountId(fromAccountId);
        transaction.setToAccountId(toAccountId);
        transaction.setAmount(amount);
        transactionRepository.saveAndFlush(transaction);

        if (fromAccountId != null) {
            Account accountFrom = accountRepository.findAccountById(fromAccountId);
            accountFrom.setBalance(accountFrom.getBalance() - amount);
            accountRepository.saveAndFlush(accountFrom);
        }
        if (toAccountId != null) {
            Account accountTo = accountRepository.findAccountById(toAccountId);
            accountTo.setBalance(accountTo.getBalance() + amount);
            accountRepository.saveAndFlush(accountTo);
        }

        return transactionDTOConverter.convert(transaction);
    }
}

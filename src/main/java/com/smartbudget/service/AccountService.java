package com.smartbudget.service;

import com.smartbudget.DTO.AccountDTO;
import com.smartbudget.converter.Converter;
import com.smartbudget.model.Account;
import com.smartbudget.repository.AccountRepository;
import com.smartbudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountDTO createAccount(String accountName, int balance, Long userId) {
        Account account = new Account();
        account.setName(accountName);
        account.setBalance(balance);
        account.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден")));
        accountRepository.saveAndFlush(account);
        return convertToDTO(account);
    }

    public List<AccountDTO> requestAccounts(Long userId) {
        return accountRepository.findAccountsByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void removeAccount(Long accountID, Long userId) {
        accountRepository.deleteAccountByIdAndUserId(accountID, userId);
    }

    public AccountDTO editAccountName(String newAccountName, Long accountID, Long userId) {
        Account account = accountRepository.findAccountByIdAndUserId(accountID, userId);
        if (account == null) {
            return null;
        }
        account.setName(newAccountName);
        accountRepository.saveAndFlush(account);
        return convertToDTO(account);
    }

    public AccountDTO findAccountByIdAndUserId(Long accountId, Long userId) {
        return Optional.ofNullable(accountRepository.findAccountByIdAndUserId(accountId, userId))
                .map(this::convertToDTO)
                .orElse(null);
    }

    public void deposit(Long accountId, int amount, Long userId) {
        Account account = accountRepository.findAccountByIdAndUserId(accountId, userId);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            accountRepository.saveAndFlush(account);
        }
    }

    public void withdraw(Long accountId, int amount, Long userId) {
        Account account = accountRepository.findAccountByIdAndUserId(accountId, userId);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountRepository.saveAndFlush(account);
        } else {
            throw new RuntimeException("Недостаточно средств или аккаунт не найден");
        }
    }

    private AccountDTO convertToDTO(Account account) {
        return new AccountDTO()
                .setId(account.getId())
                .setAccountName(account.getName())
                .setBalance(account.getBalance())
                .setCustomerId(account.getUser().getId());
    }
}

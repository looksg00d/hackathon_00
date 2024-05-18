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
    private final Converter<Account, AccountDTO> accountToDTOConverter;

    public AccountDTO createAccount(String accountName, int balance, Long userId) {

        Account account = new Account();
        account.setName(accountName);
        account.setBalance(balance);
        account.setUser(userRepository.findUserById(userId));
        AccountDTO accountDTO = accountToDTOConverter.convert(account);
        accountRepository.saveAndFlush(account);
        return accountDTO;
    }

    public List<AccountDTO> requestAccounts(Long userId) {

        return (accountRepository.findAccountsByUserId(userId).stream()
                .map(accountToDTOConverter::convert)
                .collect(Collectors.toList()));
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

        return accountToDTOConverter.convert(account);
    }

    public AccountDTO findAccountByIdAndUserId(Long accountId, Long userId) {

        return Optional.ofNullable(accountRepository
                .findAccountByIdAndUserId(accountId, userId))
                .map(accountToDTOConverter::convert)
                .orElse(null);

    }
}

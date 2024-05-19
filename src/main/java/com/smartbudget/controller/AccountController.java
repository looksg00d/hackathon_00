package com.smartbudget.controller;

import com.smartbudget.DTO.AccountDTO;
import com.smartbudget.repository.UserRepository;
import com.smartbudget.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<AccountDTO> getAccounts(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        return accountService.requestAccounts(userId);
    }

    @GetMapping("/{accountId}")
    public AccountDTO getAccountById(@PathVariable Long accountId, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        return accountService.findAccountByIdAndUserId(accountId, userId);
    }

    @PostMapping
    public AccountDTO createAccount(@RequestBody AccountRequest accountRequest, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        return accountService.createAccount(accountRequest.getName(), accountRequest.getBalance(), userId);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Long accountId, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        accountService.removeAccount(accountId, userId);
    }

    @PutMapping("/{accountId}")
    public AccountDTO updateAccount(@PathVariable Long accountId, @RequestBody AccountRequest accountRequest, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        return accountService.editAccountName(accountRequest.getName(), accountId, userId);
    }

    @PostMapping("/{accountId}/deposit")
    public void deposit(@PathVariable Long accountId, @RequestBody TransactionRequest transactionRequest, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        accountService.deposit(accountId, transactionRequest.getAmount(), userId);
    }

    @PostMapping("/{accountId}/withdraw")
    public void withdraw(@PathVariable Long accountId, @RequestBody TransactionRequest transactionRequest, Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        accountService.withdraw(accountId, transactionRequest.getAmount(), userId);
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return findUserIdByUsername(userDetails.getUsername());
        } else if (principal != null) {
            return findUserIdByUsername(principal.getName());
        }
        return null;
    }

    private Long findUserIdByUsername(String username) {
        return userRepository.findByUsername(username).getId();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class AccountRequest {
        private String name;
        private int balance;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TransactionRequest {
        private int amount;
    }
}

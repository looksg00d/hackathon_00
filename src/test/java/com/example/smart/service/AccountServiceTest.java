package com.example.smart.service;

import com.smartbudget.DTO.AccountDTO;
import com.smartbudget.converter.Converter;
import com.smartbudget.model.Account;
import com.smartbudget.model.User;
import com.smartbudget.repository.AccountRepository;
import com.smartbudget.repository.UserRepository;
import com.smartbudget.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    AccountService subj;

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    Converter<Account, AccountDTO> accountToDTOConverter;

    @Test
    public void createAccount_accountWasNotCreated() {

        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setId(1L);
        account.setName("Депозитный счёт");
        account.setBalance(50000);
        account.setUser(user);

        AccountDTO accountDTO = subj.createAccount("Депозитный счёт", 50000, 1L);

        assertNull(accountDTO);
        verify(accountRepository, times(0)).saveAndFlush(account);

    }

    @Test
    public void createAccount_accountWasCreated() {

        User user = new User();
        user.setId(1L);
        user.setFirstName("Ivan");
        user.setSurname("Ivanov");
        user.setEmail("ivanov@gmail.com");
        user.setPassword("12345");
        when(userRepository.findUserById(1L)).thenReturn(user);

        Account account = new Account();
        account.setName("Депозитный счёт");
        account.setBalance(50000);
        account.setUser(user);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setAccountName("Депозитный счёт");
        accountDTO.setBalance(50000);
        accountDTO.setUserId(1L);
        when(accountToDTOConverter.convert(account)).thenReturn(accountDTO);

        AccountDTO accountDTO1 = subj.createAccount("Депозитный счёт", 50000, 1L);

        assertNotNull(accountDTO1);
        assertEquals(accountDTO, accountDTO1);

        verify(accountRepository, times(1)).saveAndFlush(account);
        verify(accountToDTOConverter, times(1)).convert(account);

    }

    @Test
    public void requestAccounts() {

        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setName("1");
        Account account2 = new Account();
        account2.setName("2");
        Account account3 = new Account();
        account3.setName("3");

        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        when(accountRepository.findAccountsByUserId(1L)).thenReturn(accounts);

        List<AccountDTO> accountDTOs = new ArrayList<>();
        AccountDTO accountDTO1 = new AccountDTO();
        accountDTO1.setAccountName("1");
        AccountDTO accountDTO2 = new AccountDTO();
        accountDTO2.setAccountName("2");
        AccountDTO accountDTO3 = new AccountDTO();
        accountDTO3.setAccountName("3");
        accountDTOs.add(accountDTO1);
        accountDTOs.add(accountDTO2);
        accountDTOs.add(accountDTO3);

        when(accountToDTOConverter.convert(account1)).thenReturn(accountDTO1);
        when(accountToDTOConverter.convert(account2)).thenReturn(accountDTO2);
        when(accountToDTOConverter.convert(account3)).thenReturn(accountDTO3);

        List<AccountDTO> accountDTOs2 = subj.requestAccounts(1L);
        assertNotNull(accounts);

        for (int i = 0; i < accounts.size(); i++) {
            assertEquals(accountDTOs2.get(i), accountDTOs.get(i));
        }

        verify(accountRepository, times(1)).findAccountsByUserId(1L);
        verify(accountToDTOConverter, times(1)).convert(account1);
        verify(accountToDTOConverter, times(1)).convert(account2);
        verify(accountToDTOConverter, times(1)).convert(account3);

    }

}
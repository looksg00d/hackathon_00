package com.example.smart.service;

import com.smartbudget.DTO.TransactionDTO;
import com.smartbudget.converter.Converter;
import com.smartbudget.model.Account;
import com.smartbudget.model.Transaction;
import com.smartbudget.repository.AccountRepository;
import com.smartbudget.repository.TransactionRepository;
import com.smartbudget.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @InjectMocks
    TransactionService subj;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    Converter<Transaction, TransactionDTO> transactionToDTOConverter;

    @Mock
    AccountRepository accountRepository;

    @Test
    public void moneyTransferReport() {

        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setName("1");
        Transaction transaction2 = new Transaction();
        transaction2.setName("2");
        Transaction transaction3 = new Transaction();
        transaction3.setName("3");

        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);

        Account account = new Account();
        account.setId(1L);

        List<Account> accounts = Arrays.asList(account);

        when(accountRepository.findAccountsByUserId(1L)).thenReturn(accounts);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
        when(transactionRepository.findTransactionsByDateBetween(LocalDateTime.parse("2021-12-07 06:00:00", formatter),
                LocalDateTime.parse("2021-12-10 06:00:00", formatter), 1L)).thenReturn(transactions);

        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setTransactionName("1");
        TransactionDTO transactionDTO2 = new TransactionDTO();
        transactionDTO2.setTransactionName("2");
        TransactionDTO transactionDTO3 = new TransactionDTO();
        transactionDTO3.setTransactionName("3");

        transactionDTOs.add(transactionDTO1);
        transactionDTOs.add(transactionDTO2);
        transactionDTOs.add(transactionDTO3);

        when(transactionToDTOConverter.convert(transaction1)).thenReturn(transactionDTO1);
        when(transactionToDTOConverter.convert(transaction2)).thenReturn(transactionDTO2);
        when(transactionToDTOConverter.convert(transaction3)).thenReturn(transactionDTO3);

        List<TransactionDTO> transactionDTOs2 = subj.moneyTransferReport(LocalDateTime.parse("2021-12-07 06:00:00", formatter),
                LocalDateTime.parse("2021-12-10 06:00:00", formatter), 1L);
        assertNotNull(transactionDTOs2);
        for (int i = 0; i < transactions.size(); i++) {
            assertEquals(transactionDTOs2.get(i), transactionDTOs.get(i));
        }

        verify(transactionRepository, times(1)).findTransactionsByDateBetween(LocalDateTime.parse("2021-12-07 06:00:00", formatter),
                LocalDateTime.parse("2021-12-10 06:00:00", formatter), 1L);
        verify(accountRepository, times(1)).findAccountsByUserId(1L);
        verify(transactionToDTOConverter, times(1)).convert(transaction1);
        verify(transactionToDTOConverter, times(1)).convert(transaction2);
        verify(transactionToDTOConverter, times(1)).convert(transaction3);
    }

    @Test
    public void newTransaction() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
        Transaction transaction = new Transaction();
        transaction.setName("Перевод");
        transaction.setDate(LocalDateTime.parse("2022-01-07 10:00:00", formatter));
        transaction.setFromAccountId(null);
        transaction.setToAccountId(1L);
        transaction.setAmount(60000);

        when(transactionRepository.saveAndFlush(transaction)).thenReturn(transaction);

        Account accountTo = new Account();
        accountTo.setId(1L);
        accountTo.setBalance(50000);

        when(accountRepository.findAccountsByUserId(1L)).thenReturn(Arrays.asList(accountTo));
        when(accountRepository.findAccountById(1L)).thenReturn(accountTo);
        when(accountRepository.saveAndFlush(accountTo)).thenReturn(accountTo);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setTransactionName("Перевод");
        transactionDTO.setDate(LocalDateTime.parse("2022-01-07 10:00:00", formatter));
        transactionDTO.setFromAccountId(null);
        transactionDTO.setToAccountId(1L);
        transactionDTO.setAmount(60000);
        when(transactionToDTOConverter.convert(transaction)).thenReturn(transactionDTO);

        TransactionDTO transactionDTO2 = subj.newTransaction("Перевод",
                LocalDateTime.parse("2022-01-07 10:00:00", formatter),
                null, 1L, 60000, 1L);

        verify(accountRepository, times(1)).findAccountById(1L);
        verify(transactionRepository, times(1)).saveAndFlush(transaction);
        verify(accountRepository, times(1)).saveAndFlush(accountTo);
        verify(transactionToDTOConverter, times(1)).convert(transaction);
        assertNotNull(transactionDTO2);
        assertEquals(transactionDTO, transactionDTO2);

    }
}
package com.smartbudget.converter;

import com.smartbudget.DTO.TransactionDTO;
import com.smartbudget.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionToTransactionDTOConverter implements Converter<Transaction, TransactionDTO> {
    @Override
    public TransactionDTO convert(Transaction source) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(source.getId());
        transactionDTO.setTransactionName(source.getName());
        transactionDTO.setDate(source.getDate());
        transactionDTO.setFromAccountId(source.getFromAccountId());
        transactionDTO.setToAccountId(source.getToAccountId());
        transactionDTO.setAmount(source.getAmount());

        return transactionDTO;
    }
}

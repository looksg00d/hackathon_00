package com.smartbudget.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TransactionDTO {
    private Long id;
    private String transactionName;
    private LocalDateTime date;
    private Long fromAccountId;
    private Long toAccountId;
    private int amount;

}

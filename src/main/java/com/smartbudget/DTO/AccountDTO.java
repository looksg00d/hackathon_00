package com.smartbudget.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class AccountDTO {
    private Long id;
    private String accountName;
    private int balance;
    private Long customerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDTO that = (AccountDTO) o;
        return balance == that.balance && Objects.equals(id, that.id) && Objects.equals(accountName, that.accountName) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountName, balance, customerId);
    }
}

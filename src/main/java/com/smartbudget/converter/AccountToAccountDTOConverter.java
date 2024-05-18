package com.smartbudget.converter;

import com.smartbudget.DTO.AccountDTO;
import com.smartbudget.model.Account;
import org.springframework.stereotype.Service;


@Service
public class AccountToAccountDTOConverter implements Converter<Account, AccountDTO> {

    @Override
    public AccountDTO convert(Account source) {
        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setId(source.getId());
        accountDTO.setAccountName(source.getName());
        accountDTO.setBalance(source.getBalance());
        accountDTO.setCustomerId(source.getUser().getId());

        return accountDTO;
    }
}

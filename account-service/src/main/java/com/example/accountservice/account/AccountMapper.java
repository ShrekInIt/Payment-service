package com.example.accountservice.account;

import com.example.accountservice.account.dto.AccountResponse;

public final class AccountMapper {
    public static AccountResponse toAccountResponse(Account account){
        return AccountResponse.builder()
                .id(account.id())
                .ownerName(account.ownerName())
                .currency(account.currency())
                .balance(account.balance())
                .createdAt(account.createdAt())
                .updatedAt(account.updatedAt())
                .build();
    }
}

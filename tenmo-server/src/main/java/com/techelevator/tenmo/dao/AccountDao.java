package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {
    List<Account> getAccounts();

    Account getAccountById(int accountId);

    Account getAccountByUserId(int userId);

    Account createAccount(int userId,double balance);
}

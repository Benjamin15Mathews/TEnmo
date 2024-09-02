package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface AccountDao {
    List<Account> getAccounts();

    Account getAccountById(int accountId);

    Account getAccountByUserId(int userId);

    Account createAccount(int userId,double balance);

    Account updateAccount(Account account);
}

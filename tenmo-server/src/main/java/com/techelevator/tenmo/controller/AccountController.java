package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/account/{id}")
    public Account getAccountById(@PathVariable("id") int accountId) {
       return accountService.getAccountById(accountId);
    }

    @GetMapping("/account/user/{userId}")
    public Account getAccountByUserId(@PathVariable int userId) {
       return accountService.getAccountByUserId(userId);
    }

    @GetMapping("/account/user/{accountId}/username")
    public String getAccountUsername(@PathVariable int accountId) {
        return accountService.getAccountUsername(accountId);
    }

    @PutMapping("/account/user/{accountId}/balance")
    public Account updateAccount(@PathVariable int accountId, @RequestBody Account account) {
        account.setId(accountId);
        return accountService.updateAccountBalance(account);
    }

}

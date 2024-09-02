package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    public List<Account> getAccounts() {
        return accountDao.getAccounts();
    }

    public Account getAccountById(@PathVariable int id){
        return accountDao.getAccountById(id);
    }

    public Account getAccountByUserId(@PathVariable int userId) {
        return accountDao.getAccountByUserId(userId);
    }

    public String getAccountUsername(@PathVariable int accountId) {
        return userDao.getUserById(accountDao.getAccountById(accountId).getUserId()).getUsername();
    }

    public Account updateAccountBalance(Account account) {
        return accountDao.updateAccount(account);
    }
}

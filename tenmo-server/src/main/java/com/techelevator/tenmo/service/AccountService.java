package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountDao accountDao;

    public List<Account> getAccounts() {
        return accountDao.getAccounts();
    }

    public Account getAccountById(int id){
        return accountDao.getAccountById(id);
    }

    public Account getAccountByUserId(@PathVariable int userId) {
        return accountDao.getAccountByUserId(userId);
    }
}

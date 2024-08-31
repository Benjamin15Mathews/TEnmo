package com.techelevator.tenmo.model;

import java.util.Objects;

public class Account {
    private int id;
    private int userId;
    private double balance;

    public Account(){}

    public Account(int id, int userId, double balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user_id=" + userId +
                ", balance=" + balance +
                '}';
    }
}

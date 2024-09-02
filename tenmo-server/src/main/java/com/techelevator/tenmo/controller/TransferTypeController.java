package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransferTypeController {

    @Autowired
    private TransferTypeDao transferTypeDao;

    @GetMapping("/transfertype")
    public List<TransferType> getTransferTypes() { return transferTypeDao.getTransferTypes(); }

    @GetMapping("/transfertype/{id}")
    public TransferType getTransferTypeById(@PathVariable int id) { return transferTypeDao.getTransferTypeById(id); }

    @PostMapping("transfertype")
    public TransferType createTransferType(@PathVariable String description) { return transferTypeDao.createTransferType(description); }
}

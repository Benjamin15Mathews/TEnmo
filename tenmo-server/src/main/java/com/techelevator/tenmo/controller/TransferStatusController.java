package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransferStatusController {

    @Autowired
    private TransferStatusDao transferStatusDao;

    @GetMapping("transferstatus")
    public List<TransferStatus> getAllStatuses(){ return transferStatusDao.getTransferStatuses();}

    @GetMapping("transferstatus/{id}")
    public TransferStatus getTransferStatusById(@PathVariable int id){ return transferStatusDao.getTransferStatusById(id);}

    @PostMapping("transferstatus")
    public TransferStatus createTransferStatus(@RequestBody TransferStatus transferStatus){ return transferStatusDao.createTransferStatus(transferStatus.getTransferStatusDesc());}

}

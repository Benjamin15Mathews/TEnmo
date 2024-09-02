package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransferController {

    @Autowired
    private TransferDao transferDao;

    @GetMapping("/transfer")
    public List<Transfer> getAllTransfers(){
        return transferDao.getTransfers();
    }

    @GetMapping(path = "/transfer/{id}")
    public Transfer getTransferById(@PathVariable int id){
        return transferDao.getTransferById(id);
    }


    @GetMapping( "/transfer/accountfrom/{accountFromId}")
    public List<Transfer> getTransfersByAccountFrom(@PathVariable int accountFromId){
        return transferDao.getTransfersByAccountFromId(accountFromId);
    }

    @GetMapping("/transfer/accountfrom/{accountFromId}/transfertype/{transferTypeId}")
    public List<Transfer> getTransfersByAccountFromAndTransferType(@PathVariable int accountFromId, @PathVariable int transferTypeId){
        return transferDao.getTransfersByTransferType(accountFromId, transferTypeId);
    }


    @GetMapping("/transfer/accountfrom/{accountFromId}/transferstatus/{transferStatusId}")
    public List<Transfer> getTransfersByAccountFromAndTransferStatus(@PathVariable int accountFromId, @PathVariable int transferStatusId){
        return transferDao.getTransfersByTransferType(accountFromId, transferStatusId);
    }

    @GetMapping("/transfer/accountfrom/{accountId}/history")
    public List<Transfer> getAccountFromTransferHistory(@PathVariable int accountId){
        return transferDao.getAccountFromTransferHistory(accountId);
    }

    @GetMapping("/transfer/accountto/{accountId}/history")
    public List<Transfer> getAccountToTransferHistory(@PathVariable int accountId){
        return transferDao.getTransfersByAccountToId(accountId);
    }

    @PostMapping("/transfer")
    public Transfer createTransfer(@RequestBody Transfer transfer){
        return transferDao.createTransfer(transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @PutMapping("/transfer/{id}")
    public Transfer updateTransfer(@PathVariable int id, @RequestBody Transfer transfer){
        transfer.setTransferId(id);
        return transferDao.updateTransfer(transfer);
    }


}

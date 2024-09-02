package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    TransferDao transferDao;

    public List<Transfer> getTransfers() {return transferDao.getTransfers();}

    public Transfer getTransferById(int id) {return transferDao.getTransferById(id);}

    public List<Transfer> getTransfersByAccountFromId(int accountFromId) {return transferDao.getTransfersByAccountFromId(accountFromId);}

    public List<Transfer> getTransfersByAccountToId(int accountToId) {return transferDao.getTransfersByAccountToId(accountToId);}


    public List<Transfer> getTransfersByAccountFromAndTransferType(int accountFromId,int typeId) {return transferDao.getTransfersByTransferType(accountFromId,typeId);}

    public List<Transfer> getTransfersByAccountFromAndTransferStatus(int accountFromId,int statusId) {return transferDao.getTransfersByTransferStatus(accountFromId,statusId);}

    public List<Transfer> getAccountFromTransferHistory(int accountId){
        return transferDao.getAccountFromTransferHistory(accountId);
    }

    public Transfer createTransfer(int transferTypeId, int transferStatusId,int accountFrom, int accountTo, BigDecimal amount) {return transferDao.createTransfer(transferTypeId,transferStatusId,accountFrom,accountTo,amount);}

    public Transfer updateTransfer(Transfer transfer) {return transferDao.updateTransfer(transfer);}

}

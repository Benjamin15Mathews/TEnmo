package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransferById(int id);

    List<Transfer> getTransfers();

    List<Transfer> getTransfersByAccountFromId(int accountFrom);

    List<Transfer> getTransfersByTransferStatus(int accountId, int transferStatusId);

    Transfer createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount);

    List<Transfer> getTransfersByAccountToId(int accountFromId);

    List<Transfer> getTransfersByTransferType(int accountId, int transferTypeId);

    Transfer updateTransfer(Transfer transfer);

    List<Transfer> getAccountFromTransferHistory(int accountId);
}

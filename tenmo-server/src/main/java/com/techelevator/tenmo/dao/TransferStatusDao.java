package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

import java.util.List;

public interface TransferStatusDao {

    List<TransferStatus> getTransferStatuses();

    TransferStatus getTransferStatusById(int id);

    TransferStatus createTransferStatus(String desc);
}

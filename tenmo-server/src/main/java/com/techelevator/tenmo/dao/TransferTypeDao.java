package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;

import java.util.List;

public interface TransferTypeDao {

    List<TransferType> getTransferTypes();

    TransferType getTransferTypeById(int id);

    TransferType createTransferType(String description);
}

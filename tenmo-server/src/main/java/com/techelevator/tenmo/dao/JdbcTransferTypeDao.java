package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTransferTypeDao implements TransferTypeDao{
    @Override
    public List<TransferType> getTransferTypes() {
        return List.of();
    }

    @Override
    public TransferType getTransferTypeById(int id) {
        return null;
    }

    @Override
    public TransferType createTransferType(int id, String description) {
        return null;
    }
}

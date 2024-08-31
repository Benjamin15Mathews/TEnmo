package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{
    @Override
    public List<TransferStatus> getTransferStatuses() {
        return List.of();
    }
}

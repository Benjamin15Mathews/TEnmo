package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public interface TransferDao {

    Transfer getTransferById(int id);

    List<Transfer> getSentTransfers();

    List<Transfer> getReceivedTransfers();
}

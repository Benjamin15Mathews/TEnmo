package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private TransferDao transferDao;

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){
        return transferDao.getTransferById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Transfer> getSentTransfers(){
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Transfer> getRecievedTransfers(){
        return null;
    }
}

package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements  TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        String sql = "Select transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount from transfer Where transfer_id =?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()){
                transfer = mapRowToTransfer(results);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getTransfers(){
        List<Transfer> transfers = new ArrayList<Transfer>();
        String sql = "Select transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount from transfer";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()){
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        }catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByAccountFromId(int accountFrom){
        List<Transfer> transfers = new ArrayList<Transfer>();
        String sql = "Select transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount from transfer Where account_from =?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
            while(results.next()){
               Transfer transfer = mapRowToTransfer(results);
               transfers.add(transfer);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAccountFromTransferHistory(int accountId) {
        List<Transfer> transfers = new ArrayList<Transfer>();
        String sql = "Select transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount from transfer Where account_from =?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            while(results.next()){
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
public List<Transfer> getTransfersByAccountToId(int accountToId) {
    List<Transfer> transfers = new ArrayList<>();
    String sql = "Select transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount from transfer Where account_to =?";
    try{
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountToId);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
    }catch (CannotGetJdbcConnectionException e){
        throw new DaoException("Unable to connect to server or database", e);
    }
    return transfers;
}

    @Override
    public List<Transfer> getTransfersByTransferType(int accountId, int transferTypeId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount FROM transfer WHERE account_from = ? and transfer_type_id = ?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId,transferTypeId);
            while(results.next()){
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public Transfer updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer SET transfer_type_id =?, transfer_status_id =?, account_to =?, amount =? WHERE transfer_id =?";
        try{
            int rowsAffected = jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountTo(), transfer.getAmount(), transfer.getTransferId());
            if(rowsAffected == 0){
                throw new RuntimeException("Transfer not found for update");
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }



    @Override
    public List<Transfer> getTransfersByTransferStatus(int accountId, int transferStatusId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount FROM transfer WHERE account_from = ? and transfer_status_id = ?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId,transferStatusId);
            while(results.next()){
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public Transfer createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
        Transfer transfer = null;
        // create account
        String sql = "INSERT INTO transfer (transfer_type_id,transfer_status_id,account_from,account_to,amount) VALUES (?,?,?,?,?) RETURNING transfer_id";
        try{
            int newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId,transferStatusId,accountFrom,accountTo,amount);
            transfer = getTransferById(newTransferId);
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }




    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
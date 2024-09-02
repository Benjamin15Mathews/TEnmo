package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TransferStatus> getTransferStatuses() {
        List<TransferStatus> transferStatuses = new ArrayList<TransferStatus>();
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()){
                TransferStatus transferStatus = mapRowToTransferStatus(results);
                transferStatuses.add(transferStatus);
            }
        }catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferStatuses;
    }

    @Override
    public TransferStatus getTransferStatusById(int id) {
        TransferStatus transferStatus = null;
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_id =?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()){
                transferStatus = mapRowToTransferStatus(results);
            }
        }catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferStatus;
    }

    @Override
    public TransferStatus createTransferStatus(String desc){
        TransferStatus transferStatus = null;
        String sql = "INSERT INTO transfer_status (transfer_status_desc) VALUES (?) RETURNING transfer_status_id";
        try{
            int newTransferStatusId = jdbcTemplate.queryForObject(sql, Integer.class, desc);
            transferStatus = getTransferStatusById(newTransferStatusId);
        }catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferStatus;
    }

    private TransferStatus mapRowToTransferStatus(SqlRowSet rs){
        TransferStatus transferStatus = new TransferStatus(rs.getInt("transfer_status_id"), rs.getString("transfer_status_desc"));
        return transferStatus;

    }
}

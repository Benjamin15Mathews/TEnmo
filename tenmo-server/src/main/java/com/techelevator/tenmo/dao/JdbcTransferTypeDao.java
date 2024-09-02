package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferTypeDao implements TransferTypeDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TransferType> getTransferTypes() {
        List<TransferType> transferTypes = new ArrayList<TransferType>();
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()){
                TransferType transferType = mapRowToTransferType(results);
                transferTypes.add(transferType);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferTypes;
    }

    @Override
    public TransferType getTransferTypeById(int id) {
        TransferType transferType = null;
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_id =?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()){
                transferType = mapRowToTransferType(results);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transferType;
    }

    @Override
    public TransferType createTransferType(String description) {
        TransferType transferType = null;
        String sql = "INSERT INTO transfer_type (transfer_type_desc) VALUES (?) RETURNING transfer_type_id";
        try{
            int newTransferTypeId = jdbcTemplate.queryForObject(sql, Integer.class, description);
            transferType = getTransferTypeById(newTransferTypeId);
        }catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to create transfer type");
        }
        return transferType;
    }

    private TransferType mapRowToTransferType(SqlRowSet rs){
        TransferType transferType = new TransferType(rs.getInt("transfer_type_id"), rs.getString("transfer_type_desc"));
        return transferType;
    }
}

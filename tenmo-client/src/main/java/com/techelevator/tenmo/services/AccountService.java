package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class AccountService {
    // Implement methods to interact with the accountDao for account-related operations

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public AccountService(String url) {this.baseUrl = url;}

    public List<Account> getAll(){
        List<Account> accounts = new ArrayList<>();
        try{
            ResponseEntity<List<Account>> response =
                    restTemplate.exchange(baseUrl + "/accounts",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            new ParameterizedTypeReference<List<Account>>() {});
            accounts = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return accounts;
    }

    public Account getAccountByUserId(int userId) {
        Account account = null;
        try{
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "/account/user/" +userId,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Account.class);
            account = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public Account getAccountById(int id) {
        Account account = null;
        try{
            ResponseEntity<Account> response =
                    restTemplate.exchange(baseUrl + "/account/" +id,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Account.class);
            account = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public String getAccountUsername(int accountId){
        String username = null;
        try{
            ResponseEntity<String> response =
                    restTemplate.exchange(baseUrl + "/account/user/" + accountId+"/username",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            String.class);
            username = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return username;
    }


    public Account add(Account newAccount){
        HttpEntity<Account> entity = makeAccountEntity(newAccount);

        Account returnedAccount = null;
        try{
            returnedAccount = restTemplate.postForObject(baseUrl, entity, Account.class);
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return returnedAccount;
    }

    public boolean update(Account updatedAccount){
        HttpEntity<Account> entity = makeAccountEntity(updatedAccount);
        boolean success = false;
        try{
            restTemplate.put(baseUrl + "/account/user/"+updatedAccount.getId()+"/balance", entity, Account.class);
            success = true;
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean delete(int id){
        boolean success = false;
        try{
            restTemplate.delete(baseUrl + id, makeAuthEntity(), Void.class);
            success = true;
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }
}

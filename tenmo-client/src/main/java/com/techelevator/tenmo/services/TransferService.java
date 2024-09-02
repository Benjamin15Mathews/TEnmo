package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public TransferService(String url) {this.baseUrl = url;}

    public Transfer getTransferById(int Id){
        Transfer transfer = null;
        try{
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl + "/transfer" + Id,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    private List<Transfer> getPendingTransfers(int accountToId) {
        List<Transfer> transfers = new ArrayList<Transfer>();
        try{
            ResponseEntity<List<Transfer>> response =
                    restTemplate.exchange(baseUrl + "/transfer/accountto/" + accountToId+"/history",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            new ParameterizedTypeReference<List<Transfer>>() {});
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public List<Transfer> getTransfers(){
        List<Transfer> transfers = new ArrayList<Transfer>();
        try{
            ResponseEntity<List<Transfer>> response =
                    restTemplate.exchange(baseUrl + "/transfer/",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            new ParameterizedTypeReference<List<Transfer>>() {});
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public List<Transfer> getTransfersbyFromId(int accountFromId){
        List<Transfer> transfers = new ArrayList<Transfer>();
        try{
            ResponseEntity<List<Transfer>> response =
                    restTemplate.exchange(baseUrl + "/transfer/accountfrom/"+accountFromId,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            new ParameterizedTypeReference<List<Transfer>>() {});
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public List<Transfer> getPastTransfers(int accountid) {
        List<Transfer> transfers = new ArrayList<Transfer>();
        try{
            ResponseEntity<List<Transfer>> response =
                    restTemplate.exchange(baseUrl + "/transfer/accountto/" + accountid+"/history",
                            HttpMethod.GET,
                            makeAuthEntity(),
                            new ParameterizedTypeReference<List<Transfer>>() {});
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public List<Transfer> getTransfersByStatus(int accountId,int transferStatusId){
        List<Transfer> transfers = new ArrayList<Transfer>();
        try{
            ResponseEntity<List> response =
                    restTemplate.exchange(baseUrl + "/transfer/accountfrom/" +accountId + "/transferstatus/"+transferStatusId,
                            HttpMethod.GET,
                            makeAuthEntity(),
                            List.class);
            transfers = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public TransferStatus getTransferStatusById(int id) {
        TransferStatus transferStatus = null;
        try {
            ResponseEntity<TransferStatus> response = restTemplate.getForEntity(baseUrl + "/transferstatus/" + id, TransferStatus.class);
            transferStatus = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatus;
    }

    public TransferType getTransferTypeById(int id){
        TransferType transferType = null;
        try {
            ResponseEntity<TransferType> response = restTemplate.getForEntity(baseUrl + "/transfertype/" + id, TransferType.class);
            transferType = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferType;
    }

    public void printTransferDetails(Transfer transfer,AccountService accountService) {
        System.out.println("Transfer Details:");
        System.out.printf("Id: %d\n", transfer.getTransferId());
        System.out.printf("From: %s\n", accountService.getAccountUsername(transfer.getAccountFrom()));
        System.out.printf("To: %s\n", accountService.getAccountUsername(transfer.getAccountTo()));
        System.out.printf("Type: %s\n", getTransferTypeById(transfer.getTransferTypeId()).getTransferTypeDesc());
        System.out.printf("Status: %s\n", getTransferStatusById(transfer.getTransferStatusId()).getTransferStatusDesc());
        System.out.printf("Amount: $%.2f\n", transfer.getAmount());
    }

    public void viewPendingTransfers(AccountService accountService, ConsoleService consoleService, AuthenticatedUser currentUser) {
        boolean inMenu = true;
        while (inMenu) {
            List<Transfer> transfers = getPendingTransfers(accountService.getAccountByUserId(currentUser.getUser().getId()).getId());
            System.out.println("-------------------------------------------\n" +
                    "Pending Transfers\n" +
                    "ID          To                 Amount\n" +
                    "-------------------------------------------");
            for (Transfer transfer : transfers) {
                if(transfer.getTransferStatusId() == 1){
                    System.out.printf("%-12dFrom: %-15s $ %-15.2f \n", transfer.getTransferId(),accountService.getAccountUsername(transfer.getAccountFrom()), transfer.getAmount());

                }
            }
            int transferToFind = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
            if (transferToFind == 0) {
                break;
            }
            for (Transfer transfer : transfers) {
                if (transfer.getTransferId() == transferToFind && transfer.getTransferStatusId() == 1) {
                    promptApproveOrRejectTransfer(transfer, consoleService, accountService);
                    inMenu = false;
                    break;
                }
            }
            if (transferToFind != 0 && !transfers.stream().anyMatch(t -> t.getTransferId() == transferToFind)) {
                System.out.println("Please enter a valid Transfer Id or 0 to exit");
            }
        }
}



    public void promptApproveOrRejectTransfer(Transfer transfer, ConsoleService consoleService, AccountService accountService) {
    int selection;
    do {
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");
        selection = consoleService.promptForInt("Please choose an option: ");
    } while (selection < 0 || selection > 2);

    switch (selection) {
        case 1:
            transfer.setTransferStatusId(2);
            updateTransfer(transfer);
            Account accountFrom = accountService.getAccountById(transfer.getAccountFrom());
            accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getAmount()));
            Account accountTo = accountService.getAccountById(transfer.getAccountTo());
            accountTo.setBalance(accountTo.getBalance().add(transfer.getAmount()));
            accountService.update(accountFrom);
            accountService.update(accountTo);
            System.out.printf("Transfer %d Approved.%n", transfer.getTransferId());
            break;
        case 2:
            transfer.setTransferStatusId(3);
            updateTransfer(transfer);
            System.out.printf("Transfer %d Rejected.%n", transfer.getTransferId());
            break;
        case 0:
            break;
        default:
            System.out.println("Invalid option. Please choose again.");
    }
}

    public void updateTransfer(Transfer transfer) {
    try {
        restTemplate.exchange(baseUrl + "/transfer/" + transfer.getTransferId(), HttpMethod.PUT, makeAuthEntity(transfer), Transfer.class);
        BasicLogger.log("Transfer updated successfully");
    } catch (Exception e) {
        BasicLogger.log(e.getMessage());
    }
}



    public void viewTransferHistory(AccountService accountService, ConsoleService consoleService, AuthenticatedUser currentUser) {
    boolean inMenu = true;
    while (inMenu) {
        List<Transfer> transfers = getTransfers();
        System.out.println("-------------------------------------------\n" +
                "Transfers\n" +
                "ID          From/To                 Amount\n" +
                "-------------------------------------------");
        for (Transfer transfer : transfers) {
            if (transfer.getTransferTypeId() == 1 && (transfer.getAccountTo() == accountService.getAccountByUserId(currentUser.getUser().getId()).getId() | transfer.getAccountFrom() == accountService.getAccountByUserId(currentUser.getUser().getId()).getId())) {
                System.out.printf("%-12dFrom: %-15s$ %-15.2f%n", transfer.getTransferId(), accountService.getAccountUsername(transfer.getAccountTo()), transfer.getAmount());
            }
            if (transfer.getTransferTypeId() == 2 && transfer.getAccountFrom() == accountService.getAccountByUserId(currentUser.getUser().getId()).getId()) {
                System.out.printf("%-12dTo: %-15s$ %-15.2f%n", transfer.getTransferId(), accountService.getAccountUsername(transfer.getAccountTo()), transfer.getAmount());
            }
        }
        int transferToFind = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        if (transferToFind == 0) {
            return;
        } else {
            for (Transfer transfer : transfers) {
                if (transfer.getTransferId() == transferToFind) {
                    printTransferDetails(transfer, accountService);
                    inMenu = false;
                    break;
                }
            }
            if (inMenu) {
                System.out.println("Transfer not found. Please select a valid transfer Id from the list.");
            }
        }
    }
}

    public void viewCurrentBalance(AccountService accountService, ConsoleService consoleService, AuthenticatedUser currentUser) {
        try {
            BigDecimal balance = accountService.getAccountByUserId(currentUser.getUser().getId()).getBalance();
            System.out.println((String.format("Your current account balance is: %.2f", balance)));
        }catch(Exception e){
            consoleService.printErrorMessage();
        }

    }

    private HttpEntity<?> makeAuthEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<?> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private void createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amountToSend) {
    Transfer transfer = new Transfer();
    transfer.setTransferTypeId(transferTypeId);
    transfer.setTransferStatusId(transferStatusId);
    transfer.setAccountFrom(accountFrom);
    transfer.setAccountTo(accountTo);
    transfer.setAmount(amountToSend);

    try {
        restTemplate.postForEntity(baseUrl + "/transfer", makeAuthEntity(transfer), Transfer.class);
        BasicLogger.log("Transfer created successfully");
    } catch (Exception e) {
        BasicLogger.log(e.getMessage());
    }
}

    public void sendBucks(AccountService accountService, ConsoleService consoleService,AuthenticatedUser currentUser) {
        List<Account> accounts = accountService.getAll();
        Account currentAccount = accountService.getAccountByUserId(currentUser.getUser().getId());
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("-------------------------------------------\n" +
                    "Users\n" +
                    "ID          Name\n" +
                    "-------------------------------------------");

            for (Account account : accounts) {
                if (account.getId() != currentAccount.getId()) {
                    System.out.printf("%-12d%s\n", account.getId(), accountService.getAccountUsername(account.getId()));
                }
            }

            int accountToSendId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
            if (accountToSendId == 0) {
                inMenu = false;
                continue;
            }

            Account accountToSend = accountService.getAccountById(accountToSendId);
            if (accountToSend == null || accountToSend.getId() == currentAccount.getId()) {
                System.out.println("Invalid user ID. Please enter a valid user id.");
                continue;
            }

            BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter amount: ");
            if (currentAccount.getBalance().compareTo(amountToSend) < 0) {
                System.out.println("Not enough money in account to send. Please enter an amount less or equal to your balance.");
                continue;
            }

            accountToSend.setBalance(accountToSend.getBalance().add(amountToSend));
            currentAccount.setBalance(currentAccount.getBalance().subtract(amountToSend));
            accountService.update(currentAccount);
            accountService.update(accountToSend);

            createTransfer(2, 2, currentAccount.getId(), accountToSend.getId(), amountToSend);
            System.out.printf("Transfer sent successfully to %s.\n", accountService.getAccountUsername(accountToSendId));
            inMenu = false;
        }
    }


    public void requestBucks(AccountService accountService, ConsoleService consoleService, AuthenticatedUser currentUser) {
        List<Account> accounts = accountService.getAll();
        Account currentAccount = accountService.getAccountByUserId(currentUser.getUser().getId());
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("-------------------------------------------\n" +
                    "Users\n" +
                    "ID          Name\n" +
                    "-------------------------------------------");

            for (Account account : accounts) {
                if (account.getId() != currentAccount.getId()) {
                    System.out.printf("%-12d%s\n", account.getId(), accountService.getAccountUsername(account.getId()));
                }
            }

            int accountToSendId = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
            if (accountToSendId == 0) {
                inMenu = false;
                continue;
            }

            Account accountToSend = accountService.getAccountById(accountToSendId);
            if (accountToSend == null || accountToSend.getId() == currentAccount.getId()) {
                System.out.println("Invalid user ID. Please enter a valid user id.");
                continue;
            }


            BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter amount: ");

            createTransfer(1, 1, currentAccount.getId(), accountToSend.getId(), amountToSend);
            System.out.printf("Request sent successfully to %s.\n", accountService.getAccountUsername(accountToSendId));
            inMenu = false;


        }

    }
}

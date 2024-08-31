
# TEnmo - Online Payment Service

TEnmo is an online payment service designed to allow users to transfer "TE Bucks" between friends. This project involves creating a RESTful API server and a command-line application that supports various functionalities, such as registering users, logging in, viewing balances, and sending or requesting TE Bucks.

## Table of Contents

- [Use Cases](#use-cases)
- [Sample Screens](#sample-screens)
- [Database Schema](#database-schema)
- [How to Set Up the Database](#how-to-set-up-the-database)

## Use Cases

1. **User Registration**: Users can register with a username and password, starting with a balance of 1,000 TE Bucks.
2. **User Login**: Users can log in using their registered credentials and receive an authentication token for subsequent interactions.
3. **View Account Balance**: Authenticated users can view their current account balance.
4. **Send TE Bucks**: Authenticated users can send a specified amount of TE Bucks to another user.
   - Users cannot send TE Bucks to themselves.
   - Transfers include the IDs of the sender and receiver, and the amount.
   - The sender’s balance decreases, and the receiver’s balance increases.
   - Transfers cannot exceed the sender’s current balance or be zero/negative amounts.
5. **View Transfers**: Users can view all transfers they've sent or received.
6. **Transfer Details**: Users can view details of a specific transfer by its ID.
7. **Request TE Bucks**: Users can request a specified amount of TE Bucks from another user.
   - Users cannot request TE Bucks from themselves.
   - The request remains pending until approved or rejected.
8. **View Pending Transfers**: Users can view their pending transfer requests.
9. **Approve or Reject Requests**: Users can approve or reject pending transfer requests.
   - Approvals are contingent on sufficient balance.
   - Approved transfers adjust the balances of the requester and requestee; rejected transfers do not affect balances.

## Sample Screens

### View Current Balance
```
Your current account balance is: $9999.99
```

### Send TE Bucks
```
-------------------------------------------
Users
ID          Name
-------------------------------------------
313         Bernice
54          Larry
---------

Enter ID of user you are sending to (0 to cancel):
Enter amount:
```

### View Transfers
```
-------------------------------------------
Transfers
ID          From/To                 Amount
-------------------------------------------
23          From: Bernice          $ 903.14
79          To:    Larry           $  12.55
---------
Please enter transfer ID to view details (0 to cancel):
```

### Transfer Details
```
--------------------------------------------
Transfer Details
--------------------------------------------
 Id: 23
 From: Bernice
 To: Me Myselfandi
 Type: Send
 Status: Approved
 Amount: $903.14
```

### Request TE Bucks
```
-------------------------------------------
Users
ID          Name
-------------------------------------------
313         Bernice
54          Larry
---------

Enter ID of user you are requesting from (0 to cancel):
Enter amount:
```

### Pending Requests
```
-------------------------------------------
Pending Transfers
ID          To                     Amount
-------------------------------------------
88          Bernice                $ 142.56
147         Larry                  $  10.17
---------
Please enter transfer ID to approve/reject (0 to cancel):
```

### Approve or Reject Pending Transfer
```
1: Approve
2: Reject
0: Don't approve or reject
---------
Please choose an option:
```

## Database Schema

### Tables

- **`tenmo_user`**: Stores user login information.

| Field           | Description                                                                    |
|-----------------|--------------------------------------------------------------------------------|
| `user_id`       | Unique identifier of the user                                                  |
| `username`      | String that identifies the name of the user; used as part of the login process |
| `password_hash` | Hashed version of the user's password                                          |
| `role`          | Name of the user's role                                                        |

- **`account`**: Tracks users' TE Bucks balances.

| Field        | Description                                                        |
|--------------|--------------------------------------------------------------------|
| `account_id` | Unique identifier of the account                                   |
| `user_id`    | Foreign key to the `users` table; identifies user who owns account |
| `balance`    | The amount of TE bucks currently in the account                    |

- **`transfer_type`**: Defines transfer types (Request or Send).

| Field                | Description                             |
|----------------------|-----------------------------------------|
| `transfer_type_id`   | Unique identifier of the transfer type  |
| `transfer_type_desc` | String description of the transfer type |

| `transfer_type_id` | `transfer_type_desc` | Purpose                                                               |
|--------------------|----------------------|-----------------------------------------------------------------------|
| 1                  | Request              | Identifies transfer where a user requests money from another user     |
| 2                  | Send                 | Identifies transfer where a user sends money to another user          |

- **`transfer_status`**: Defines statuses of transfers (Pending, Approved, Rejected).

| Field                  | Description                               |
|------------------------|-------------------------------------------|
| `transfer_status_id`   | Unique identifier of the transfer status  |
| `transfer_status_desc` | String description of the transfer status |

| `transfer_status_id` | `transfer_status_desc` | Purpose                                                               |
|----------------------|------------------------|-----------------------------------------------------------------------|
| 1                    | Pending                | Identifies transfer that hasn't occurred yet and requires approval    |
| 2                    | Approved               | Identifies transfer that has been approved and occurred               |
| 3                    | Rejected               | Identifies transfer that wasn't approved                              |

- **`transfer`**: Records details of TE Bucks transfers.

| Field                | Description                                                                                     |
|----------------------|-------------------------------------------------------------------------------------------------|
| `transfer_id`        | Unique identifier of the transfer                                                               |
| `transfer_type_id`   | Foreign key to the `transfer_types` table; identifies type of transfer                          |
| `transfer_status_id` | Foreign key to the `transfer_statuses` table; identifies status of transfer                     |
| `account_from`       | Foreign key to the `accounts` table; identifies the account that the funds are being taken from |
| `account_to`         | Foreign key to the `accounts` table; identifies the account that the funds are going to         |
| `amount`             | Amount of the transfer                                                                          |

## How to Set Up the Database

1. Create a new PostgreSQL database named `tenmo`.
2. Run the SQL script located at `database/tenmo.sql` to set up the required tables and constraints.

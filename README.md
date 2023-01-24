# Wallet Microservice Project

### wallet-naming-server:
&nbsp;&nbsp;&nbsp;&nbsp;Eureka Naming Server for the microservices

### wallet-gateway:
&nbsp;&nbsp;&nbsp;&nbsp;API Gateway

### user-service
&nbsp;&nbsp;&nbsp;&nbsp;User microservice that handles User reading, creation, update and deletion.

### wallet-service:
&nbsp;&nbsp;&nbsp;&nbsp;Wallet mircoservice that handles Wallet reading, creation, balance updating and deletion.

### transaction-service:
&nbsp;&nbsp;&nbsp;&nbsp;Transaction microservice that handles Transaction reading, creation and Transaction history.
<br /><br />
## Endpoints
  
### **User Service:**
**Get User by User ID**\
GET http://localhost:9090/user-service/api/user/{id}

**Get Wallet for User by User ID**\
GET http://localhost:9090/user-service/api/user/{id}/wallet

**Create User**\
POST http://localhost:9090/user-service/api/user/create-user

```json
{
    "firstName":"NewUser",
    "lastName":"Name",
    "email":"un@un.com",
    "password":"UserName2023!",
    "dateOfBirth":"1987-05-10",
    "phoneNumber":"12345678",
    "zipCode":"2200",
    "city":"København",
    "address":"Nørrebrogade 12",
    "country":"Denmark"
}
```

**Update User**\
PUT http://localhost:9090/user-service/api/user/{id}/update-user

```json
{
    "firstName":"UpdatedUSer",
    "lastName":"Name",
    "email":"un@un.com",
    "password":"UserName2023!",
    "dateOfBirth":"1987-05-10",
    "phoneNumber":"12345678",
    "zipCode":"2200",
    "city":"København",
    "address":"Nørrebrogade 12",
    "country":"Denmark"
}
```

**Delete User**\
DELETE http://localhost:9090/user-service/api/user/{id}/delete-user
<br /><br />
### **Wallet Service:**
**Get Wallet by Wallet ID**\
GET http://localhost:9090/wallet-service/api/wallet/{id}

**Create Wallet**\
POST http://localhost:9090/wallet-service/api/wallet/user/{userId}/create-wallet

**Update Wallet Balance**\
PUT http://localhost:9090/wallet-service/api/wallet/update-wallet

```json
{
    "walletId" : 1,
    "amount" : "5000",
    "transactionType" : "DEPOSIT" or "WITHDRAW"
}
```

**Delete Wallet**\
DELETE http://localhost:9090/wallet-service/api/wallet/{id}/delete-wallet
<br /><br />
### **Transaction Service:**
**Get Transaction by Transaction ID and Wallet ID**\
GET http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}/{id}

**Get all Transactions by Wallet ID**\
GET http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}

**Get all Transactions by Wallet ID between two dates**\
GET http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}/from/{fromDate}/to/{toDate}

**Create Transaction**\
POST http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}/create-transaction

```json
{
    "walletId" : 1,
    "amount" : "5000",
    "transactionType" : "DEPOSIT" or "WITHDRAW"
}
```

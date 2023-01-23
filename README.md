# Wallet Microservice Project

### wallet-naming-server:
Eureka Naming Server for the microservices

### wallet-gateway:
API Gateway

### user-service
User microservice that handles User creation, update and deletion.

### wallet-service:
Wallet mircoservice that handles Wallet creation, balance updating and deletion.

### transaction-service:
Transaction microservice that handles Transaction creation and Transaction history.


## Endpoints

### **User Service:**
Get User by User ID\
GET http://localhost:9090/user-service/api/user/{id}

Get Wallet for User by User ID\
GET http://localhost:9090/user-service/api/user/{id}/wallet

Create User\
POST http://localhost:9090/user-service/api/user/create-user

Update User\
PUT http://localhost:9090/user-service/api/user/{id}/update-user

Delete User\
DELETE http://localhost:9090/user-service/api/user/{id}/delete-user

### **Wallet Service:**
Get Wallet by Wallet ID\
GET http://localhost:9090/wallet-service/api/wallet/{id}

Create Wallet\
POST http://localhost:9090/wallet-service/api/wallet/user/{userId}/create-wallet

Update Wallet Balance\
PUT http://localhost:9090/wallet-service/api/wallet/update-wallet

Delete Wallet\
DELETE http://localhost:9090/wallet-service/api/wallet/{id}/delete-wallet

### **Transaction Service:**
Get Transaction by Transaction ID and Wallet ID\
GET http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}/{id}

Get all Transactions by Wallet ID\
GET http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}

Get all Transactions by Wallet ID between two dates\
GET http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}/from/{fromDate}/to/{toDate}

Create Transaction\
POST http://localhost:9090/transaction-service/api/transaction/wallet/{walletId}/create-transaction

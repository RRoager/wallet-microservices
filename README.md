# Wallet Microservice Project

### wallet-naming-server:
Eureka Naming Server for the microservices\

### wallet-gateway:
API Gateway\

### user-service
User microservice that handles User creation.\

### wallet-service:
Wallet mircoservice that handles Wallet Creation and Balance updating.\

### transaction-service:
Transaction microservice that handles Transaction creation and Transaction history.\
\

## Endpoints

### **User Service:**
Get User by User ID\
GET http://localhost:9090/user-service/api/user/{id}\

Get Wallet for User by User ID\
GET http://localhost:9090/user-service/api/user/{id}/wallet\

Create User\
POST http://localhost:9090/user-service/api/user/create-user\
\
### **Wallet Service:**
Get Wallet by Wallet ID\
GET http://localhost:9090/wallet-service/api/wallet/{id}\

Create Wallet\
POST http://localhost:9090/wallet-service/api/wallet/create-wallet\

Update Wallet Balance\
PUT http://localhost:9090/wallet-service/api/wallet/update-wallet\
\
### **Transaction Service:**
Get Transaction by Transaction ID and Wallet ID\
GET http://localhost:9090/transaction-service/api/transaction/{walletId}/{id}\

Get all Transactions by Wallet ID\
GET http://localhost:9090/transaction-service/api/transaction/{walletId}\

Get all Transactions by Wallet ID between two dates\
GET http://localhost:9090/transaction-service/api/transaction/{walletId}/from/{fromDate}/to/{toDate}\

Create Transaction\
POST http://localhost:9090/transaction-service/api/transaction/{walletId}/create-transaction\

insert into WALLET (id, balance) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 5000);
insert into WALLET (id, balance) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 15000);
insert into WALLET (id, balance) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 2500);

insert into TRANSACTION (amount, date, transaction_type) values (1500, '2023-01-01', 1);
insert into TRANSACTION (amount, date, transaction_type) values (500, '2023-01-03', 2);
insert into TRANSACTION (amount, date, transaction_type) values (100, '2023-01-08', 2);
insert into TRANSACTION (amount, date, transaction_type) values (300, '2023-01-02', 1);
insert into TRANSACTION (amount, date, transaction_type) values (250, '2023-01-05', 2);
insert into TRANSACTION (amount, date, transaction_type) values (800, '2023-01-06', 1);
insert into TRANSACTION (amount, date, transaction_type) values (600, '2023-01-03', 2);
insert into TRANSACTION (amount, date, transaction_type) values (5000, '2023-01-05', 1);
insert into TRANSACTION (amount, date, transaction_type) values (2500, '2023-01-10', 1);

insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 1);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 2);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 3);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 4);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 5);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 6);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 7);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 8);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 9);


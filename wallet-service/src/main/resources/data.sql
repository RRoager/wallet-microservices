insert into WALLET (id, balance) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 5000.0);
insert into WALLET (id, balance) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 15000.0);
insert into WALLET (id, balance) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 2500.0);

insert into TRANSACTION (amount, transaction_date, transaction_type) values (1500.0, '2023-01-01', 'DEPOSIT');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (500.0, '2023-01-03', 'WITHDRAW');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (100.0, '2023-01-08', 'WITHDRAW');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (300.0, '2023-01-02', 'DEPOSIT');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (250.0, '2023-01-05', 'WITHDRAW');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (800.0, '2023-01-06', 'DEPOSIT');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (600.0, '2023-01-03', 'WITHDRAW');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (5000.0, '2023-01-05', 'DEPOSIT');
insert into TRANSACTION (amount, transaction_date, transaction_type) values (2500.0, '2023-01-10', 'DEPOSIT');

insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 1);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 2);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('f753f2cc-1e67-4ad0-b618-ed9450badf0e', 3);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 4);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 5);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a200158e-b240-4e84-b88d-5ab40b3aec1c', 6);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 7);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 8);
insert into WALLET_TRANSACTIONS (wallet_id, transactions_id) values ('a2222811-c5e9-439e-b129-cf126a7eff09', 9);


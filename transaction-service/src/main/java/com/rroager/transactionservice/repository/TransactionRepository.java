package com.rroager.transactionservice.repository;

import com.rroager.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Optional<Transaction> findByIdAndWalletId(Integer id, Integer walletId);

    List<Transaction> findAllByWalletId(Integer id);

    @Query("select t from Transaction t where t.walletId = :walletId and t.transactionDate between :fromDate and :toDate")
    List<Transaction> findAllByWalletIdFromDateToDate(Integer walletId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}

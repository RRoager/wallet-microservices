package com.rroager.transactionservice.feign;

import com.rroager.transactionservice.entity.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@org.springframework.cloud.openfeign.FeignClient(value = "wallet-gateway")
public interface FeignClient {

    @PutMapping("wallet-service/api/wallet/update-wallet")
    ResponseEntity<String> updateWalletBalance(@RequestBody Transaction transaction);
}
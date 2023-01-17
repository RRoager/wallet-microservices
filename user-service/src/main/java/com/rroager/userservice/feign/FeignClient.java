package com.rroager.userservice.feign;

import com.rroager.userservice.response.WalletResponse;
import org.springframework.web.bind.annotation.*;

// Using Api Gateway to request from other microservices
@org.springframework.cloud.openfeign.FeignClient(value = "wallet-gateway")
public interface FeignClient {

    // Referring to the getById method in WalletService WalletController, which makes up able to call it from the UserService
    // wallet-service in the URL refers to the microservices name in the Eureka Discovery Server
    @GetMapping("wallet-service/api/wallet/{id}")
    public WalletResponse getWalletById(@PathVariable String id);

    @PostMapping("wallet-service/api/wallet/create-wallet")
    public WalletResponse createWallet(@RequestBody String id);
}

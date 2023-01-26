package com.rroager.userservice.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WalletResponse {
    private Integer id;
    private Integer userId;
    private Double balance;
}

package com.example.paymentservice.accountclient;

import com.example.AccountTransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class AccountClient {

    private final RestClient restClient;

    public AccountClient(
            @Value("${app.account-service.base-url}") String accountServiceBaseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(accountServiceBaseUrl)
                .build();
    }

    public void transfer(AccountTransferRequest request) {
        log.info("Calling account-service transfer: referenceId={}", request.referenceId());

        restClient.post()
                .uri("/accounts/transfer")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}

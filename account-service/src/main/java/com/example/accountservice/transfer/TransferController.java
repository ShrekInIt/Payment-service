package com.example.accountservice.transfer;

import com.example.accountservice.transfer.dto.TransferRequest;
import com.example.accountservice.transfer.dto.TransferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts/transfer")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
    @RequestBody @Valid TransferRequest request
    ) {
        log.info("Received transfer request: {}", request);
        var response = transferService.transfer(request);
        log.info("Transfer completed successfully: {}", response);
        return ResponseEntity.ok(response);
    }
}

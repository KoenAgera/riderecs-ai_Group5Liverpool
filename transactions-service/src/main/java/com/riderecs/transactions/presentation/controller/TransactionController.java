package com.riderecs.transactions.presentation.controller;

import com.riderecs.transactions.application.service.TransactionService;
import com.riderecs.transactions.domain.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBuyerId(@PathVariable Long buyerId) {
        List<Transaction> transactions = transactionService.getTransactionsByBuyerId(buyerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Transaction>> getTransactionsBySellerId(@PathVariable Long sellerId) {
        List<Transaction> transactions = transactionService.getTransactionsBySellerId(sellerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCarId(@PathVariable Long carId) {
        List<Transaction> transactions = transactionService.getTransactionsByCarId(carId);
        return ResponseEntity.ok(transactions);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Transaction> updateTransactionStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        Transaction updatedTransaction = transactionService.updateTransactionStatus(id, request.getStatus());
        if (updatedTransaction != null) {
            return ResponseEntity.ok(updatedTransaction);
        }
        return ResponseEntity.notFound().build();
    }

    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

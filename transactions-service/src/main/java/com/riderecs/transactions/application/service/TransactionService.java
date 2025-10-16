package com.riderecs.transactions.application.service;

import com.riderecs.transactions.domain.model.Transaction;
import com.riderecs.transactions.infrastructure.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByBuyerId(Long buyerId) {
        return transactionRepository.findByBuyerId(buyerId);
    }

    public List<Transaction> getTransactionsBySellerId(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }

    public List<Transaction> getTransactionsByCarId(Long carId) {
        return transactionRepository.findByCarId(carId);
    }

    @Transactional
    public Transaction updateTransactionStatus(Long id, String status) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            transaction.setStatus(status);
            return transactionRepository.save(transaction);
        }
        return null;
    }
}

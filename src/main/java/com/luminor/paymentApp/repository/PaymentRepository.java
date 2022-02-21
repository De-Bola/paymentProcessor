package com.luminor.paymentApp.repository;

import com.luminor.paymentApp.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, UUID> {
    Page<Payment> getPaymentsByDebtorIban(String debtorIban, Pageable pageable);
}

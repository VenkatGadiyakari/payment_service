package com.ticketing.payment.repository;

import com.ticketing.payment.entity.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentEventRepository extends JpaRepository<PaymentEvent, UUID> {

    boolean existsByStripeEventId(String stripeEventId);
}

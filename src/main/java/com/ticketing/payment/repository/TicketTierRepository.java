package com.ticketing.payment.repository;

import com.ticketing.payment.entity.TicketTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketTierRepository extends JpaRepository<TicketTier, UUID> {

    @Modifying
    @Query("UPDATE TicketTier t SET t.remainingQty = t.remainingQty - :quantity " +
           "WHERE t.id = :tierId AND t.remainingQty >= :quantity")
    int decrementInventory(@Param("tierId") UUID tierId, @Param("quantity") Integer quantity);
}

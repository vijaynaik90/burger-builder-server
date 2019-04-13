package com.burgerapp.repositories;


import com.burgerapp.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o from Order o WHERE o.user.id = :userId AND o.archived = false")
    List<Order> findByUserIdAndUnArchived(@Param("userId") Long userId);

    @Query("SELECT o from Order o WHERE o.user.id = :userId AND o.archived = true")
    List<Order> findByUserIdAndArchived(@Param("userId") Long userId);

    @Query("SELECT o from Order o WHERE o.user.id = :userId AND o.id = :orderId")
    Order findByUserIdAndOrderId(@Param("userId") Long userId,@Param("orderId") Long orderId);
}

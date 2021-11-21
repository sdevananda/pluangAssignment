package com.example.trading.repository;

import com.example.trading.entity.BuyOrder;
import com.example.trading.entity.SellOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.List;

@Repository
public interface BuyOrderRepo extends JpaRepository<BuyOrder,Long> {

    @Query("SELECT u FROM BuyOrder u WHERE (u.price >= :price AND u.name= :name AND NOT(u.status = 'COMPLETE')) ORDER BY u.price DESC, u.time ASC")
    List<BuyOrder> findOrdersGreaterthan(@Param("price") double price, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "update BuyOrder u set u.quantity=:quantity, u.status=:status where u.id=:id")
    void updateQuantityAndStatus(@Param("quantity") double quantity,
                                 @Param("status") String status,
                                 @Param("id") Long id);


    @Modifying
    @Transactional
    @Query(value = "update BuyOrder u set u.status=:status where u.id=:id")
    void updateStatus(@Param("status") String status,
                      @Param("id") Long id);
}

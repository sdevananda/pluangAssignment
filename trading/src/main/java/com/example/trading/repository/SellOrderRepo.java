package com.example.trading.repository;

import com.example.trading.entity.SellOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.List;

@Repository
public interface SellOrderRepo extends JpaRepository<SellOrder,Long> {

//    @Query(value = "select * from sellorder where price=:price and name := name", nativeQuery = true)
//    List<SellOrder> findOrdersLessthan(@Param("price") Long price,
//                                             @Param("name") String name);

    @Query("SELECT u FROM SellOrder u WHERE u.price <= :price AND u.name= :name AND  NOT(u.status = 'COMPLETE') ORDER BY u.price ASC , u.time DESC")
    List<SellOrder> findOrdersLessthan(@Param("price") double price, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "update SellOrder u set u.quantity=:quantity, u.status=:status where u.id=:id")
    void updateQuantityAndStatus(@Param("quantity") double quantity,
                                 @Param("status") String status,
                                 @Param("id") Long id);


    //void updateStatus(String done, SellOrder sellOrder);
    @Modifying
    @Transactional
    @Query(value = "update SellOrder u set u.status=:status where u.id=:id")
    void updateStatus(@Param("status") String status,
                      @Param("id") Long id);
}

package com.cashmanager.back.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cashmanager.back.api.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	@Query(value = "select * from cart where cart.user_id=:id and cart.ispayed=true", nativeQuery=true)
	List<Cart> getUserCarts(@Param("id") Long id);
	
	@Query(value = "select * from cart where cart.user_id=:id and cart.ispayed=false", nativeQuery=true)
	List<Cart> getUserCart(@Param("id") Long id);
}

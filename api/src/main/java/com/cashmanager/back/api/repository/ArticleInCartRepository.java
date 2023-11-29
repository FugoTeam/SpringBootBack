package com.cashmanager.back.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cashmanager.back.api.model.ArticleInCart;

public interface ArticleInCartRepository extends JpaRepository<ArticleInCart, Long> {

}

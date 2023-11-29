package com.cashmanager.back.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cashmanager.back.api.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}

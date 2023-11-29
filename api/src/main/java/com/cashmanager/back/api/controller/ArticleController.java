package com.cashmanager.back.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cashmanager.back.api.model.Article;
import com.cashmanager.back.api.repository.ArticleRepository;

@RestController
@RequestMapping("/api")
public class ArticleController {
	@Autowired
	ArticleRepository articleRepository;

	// Get all articles
	@GetMapping("/articles")
	public List<Article> getAllArticles(){
		return articleRepository.findAll();
	}
	
	// Create an article
	@PostMapping("/articles")
	public Article createArticle(@Valid @RequestBody Article article) {
	    return articleRepository.save(article);
	}
	
	// Get a Single Article
	@GetMapping("/articles/{id}")
	public Article getArticleById(@PathVariable(value = "id") Long articleId) {
		Optional<Article> article = articleRepository.findById(articleId);
	    return article.get();
	}
	
	// Put a Article
	@PutMapping("/articles/{id}")
	public Article updateArticle(@PathVariable(value = "id") Long articleId, @Valid @RequestBody Article articleDetails) throws Exception{
		Article article = articleRepository.findById(articleId).get();
		if(articleDetails.getName()!=null) {
			article.setName(articleDetails.getName());
		}
		if(articleDetails.getPrice()!=null) {
			article.setPrice(articleDetails.getPrice());
		}
		Article updatedArticle = articleRepository.save(article);
	    return updatedArticle;
	}
	
	// Delete a Article
	@DeleteMapping("/articles/{id}")
	public ResponseEntity<?> deleteArticle(@PathVariable(value = "id") Long articleId) throws Exception{
		articleRepository.deleteById(articleId);
		return ResponseEntity.ok().build();
	}
}

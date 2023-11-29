package com.cashmanager.back.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "articleincart")
public class ArticleInCart {
	
	@Id
    @GeneratedValue
	private long id;
	
	@ManyToOne
	private Article article;
	
	@Column(name = "NBARTICLE", nullable = false)
	private int nbArticle;

	public ArticleInCart() {
		super();
	}

	public ArticleInCart(Article article, int nbArticle) {
		super();
		this.article=article;
		this.nbArticle=nbArticle;
	}

	public ArticleInCart(Article article) {
		super();
		this.article=article;
		this.nbArticle=1;
	}
	
	public long getId() {
		return id;
	}

	public long getArticleId() {
		return this.article.getId();
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public int getNbArticle() {
		return nbArticle;
	}

	public void setNbArticle(int nbArticle) {
		this.nbArticle = nbArticle;
	}

	public int addArticle(int nbArticle) {
		this.nbArticle+=nbArticle;
		return this.nbArticle;
	}
	
	public int removeArticle() {
		return --this.nbArticle;
	}
}

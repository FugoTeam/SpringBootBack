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

import com.cashmanager.back.api.exception.ResourceNotFoundException;
import com.cashmanager.back.api.model.Article;
import com.cashmanager.back.api.model.ArticleInCart;
import com.cashmanager.back.api.model.Cart;
import com.cashmanager.back.api.model.User;
import com.cashmanager.back.api.repository.ArticleInCartRepository;
import com.cashmanager.back.api.repository.ArticleRepository;
import com.cashmanager.back.api.repository.CartRepository;
import com.cashmanager.back.api.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class CartController {
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ArticleInCartRepository articleInCartRepository;
	
	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	//Get all cart of User
	@GetMapping("/carts/{userId}")
	public List<Cart> getAllCarts(@PathVariable(value = "userId") Long userId){
		return cartRepository.getUserCarts(userId);
	}
	
	// Get current cart of user
	@GetMapping("/cart/{userId}")
	public Cart getCartById(@PathVariable(value = "userId") Long userId) {
		List<Cart> carts = cartRepository.getUserCart(userId);
		if(carts.isEmpty()) {
			User user = userRepository.findById(userId).get();
			Cart cart = new Cart(user);
			return cartRepository.save(cart);
		}
		else {
			return carts.get(0);
		}
	}
	
	// Add article ton user cart
	@PutMapping("/carts/{id}/{articleId}/{nbArticle}")
	public Cart addArticleToCart(@PathVariable(value = "id") Long cartId, @PathVariable(value = "articleId") Long articleId, @PathVariable(value = "nbArticle") int nbArticle) throws Exception{
		Cart cart = cartRepository.findById(cartId).get();
		List<ArticleInCart> articlesInCart=cart.getArticlesInCart();
		int i=0;
		while(i<articlesInCart.size() && articlesInCart.get(i).getArticleId()!=articleId) {
			i++;
		}
		
		if(i==articlesInCart.size()) {
			Article article = articleRepository.findById(articleId).get();
			if(article==null) {
				throw (new ResourceNotFoundException("Article","Id",articleId));
			}
			ArticleInCart articleInCart = new ArticleInCart(article, nbArticle);
			cart.getArticlesInCart().add(articleInCartRepository.save(articleInCart));
			cart.setTotal(cart.getTotal()+nbArticle*article.getPrice());
		}
		else {
			articlesInCart.get(i).addArticle(nbArticle);
			cart.setTotal(cart.getTotal()+nbArticle*articlesInCart.get(i).getArticle().getPrice());
			articleInCartRepository.save(articlesInCart.get(i));
		}
		Cart updatedCart = cartRepository.save(cart);
	    return updatedCart;
	}
	
	// Delete article(s) by article id and by number and by cart id
	@DeleteMapping("/carts/{id}/{articleId}/{nbArticle}")
	public Cart changeNumberArticleInCart(@PathVariable(value = "id") Long cartId, @PathVariable(value = "articleId") Long articleId, @PathVariable(value = "nbArticle") int nbArticle) throws Exception{
		Cart cart = cartRepository.findById(cartId).get();
		List<ArticleInCart> articlesInCart=cart.getArticlesInCart();
		int i=0;
		while(i<articlesInCart.size() && articlesInCart.get(i).getArticleId()!=articleId) {
			i++;
		}
		if(i<articlesInCart.size()) {
			if(nbArticle==0) {
				long articleInCartId= articlesInCart.get(i).getId();
				articlesInCart.remove(i);
				cart.setTotal(cart.getTotal()-(articlesInCart.get(i).getNbArticle())*articlesInCart.get(i).getArticle().getPrice());
				cartRepository.save(cart);
				articleInCartRepository.deleteById(articleInCartId);
			}
			else {
				cart.setTotal(cart.getTotal()-(articlesInCart.get(i).getNbArticle()-nbArticle)*articlesInCart.get(i).getArticle().getPrice());
				articlesInCart.get(i).setNbArticle(nbArticle);
				articleInCartRepository.save(articlesInCart.get(i));
			}
		}
		Cart updatedCart = cartRepository.save(cart);
	    return updatedCart;
	}
	
	// Delete a Cart
	@DeleteMapping("/carts/{id}")
	public ResponseEntity<?> deleteCart(@PathVariable(value = "id") Long cartId) throws Exception{
		cartRepository.deleteById(cartId);
		return ResponseEntity.ok().build();
	}
}

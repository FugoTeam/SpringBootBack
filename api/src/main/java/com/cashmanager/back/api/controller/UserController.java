package com.cashmanager.back.api.controller;

import java.util.List;

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
import com.cashmanager.back.api.model.Cart;
import com.cashmanager.back.api.model.CheckOutType;
import com.cashmanager.back.api.model.User;
import com.cashmanager.back.api.repository.CartRepository;
import com.cashmanager.back.api.repository.CheckOutTypeRepository;
import com.cashmanager.back.api.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CheckOutTypeRepository checkOutTypeRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@GetMapping("/users")
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	// Create an user
	@PostMapping("/users")
	public User createUser(@Valid @RequestBody User user) {
	    return userRepository.save(user);
	}
	
	// Get a Single User
	@GetMapping("/users/{id}")
	public User getUserById(@PathVariable(value = "id") Long userId) throws Exception{
	    return userRepository.findById(userId).get();
	}
	
	// Get a User's checkOutTypes
	@GetMapping("/getCheckOutTypes/{id}")
	public List<CheckOutType> getCheckOutTypes(@PathVariable(value = "id") Long userId) throws Exception{
	    return userRepository.findById(userId).get().getCheckOutTypes();
	}
	
	// Put a User
	@PutMapping("/users/{id}")
	public User updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) throws Exception{
		User user = userRepository.findById(userId).get();
		if(userDetails.getPassword()!=null) {
			user.setPassword(userDetails.getPassword());
		}
		if(userDetails.getEmail()!=null) {
			user.setEmail(userDetails.getEmail());
		}
		if(userDetails.getUsername()!=null) {
			user.setUsername(userDetails.getUsername());
		}
		User updatedUser = userRepository.save(user);
	    return updatedUser;
	}
	
	// Delete a User
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId) throws Exception{
		userRepository.deleteById(userId);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/users/{id}/{COTid}")
	public User addExistingCheckOutType(@PathVariable(value = "id") Long userId, @PathVariable(value = "COTid") String checkOutTypeId) throws Exception{
		User user = userRepository.findById(userId).get();
		if(user==null) {
			throw (new ResourceNotFoundException("User","Id",userId));
		}
		CheckOutType checkOutType = checkOutTypeRepository.findById(checkOutTypeId).get();
		if(checkOutType==null) {
			throw (new ResourceNotFoundException("CheckOutType","Id",checkOutTypeId));
		}
		user.addCheckOutType(checkOutType);
		User updatedUser = userRepository.save(user);
	    return updatedUser;
	}
	
	
	
	@DeleteMapping("/users/{id}/{COTid}")
	public User removeCheckOutType(@PathVariable(value = "id") Long userId, @PathVariable(value = "COTid") String checkOutTypeId) throws Exception{
		User user = userRepository.findById(userId).get();
		if(user==null) {
			throw (new ResourceNotFoundException("User","Id",userId));
		}
		user.removeCheckOutType(new CheckOutType(checkOutTypeId));
		userRepository.save(user);
		return user;
	}
	
	// Pay current cart
	@PutMapping("/pay/{id}/{COTid}")
	public String payCurrentCart(@PathVariable(value = "id") Long userId, @PathVariable(value = "COTid") String cotid) throws Exception{
		Cart cart = cartRepository.getUserCart(userId).get(0);
		User user = userRepository.findById(userId).get();
		if(user==null) {
			throw (new ResourceNotFoundException("User","Id",userId));
		}
		if(cart!=null && cart.getTotal()>0.01) {
			CheckOutType cot = checkOutTypeRepository.findById(cotid).get();
			if(cot==null) {
				throw (new ResourceNotFoundException("CheckOutType","Id",cotid));
			}
			if(cot.getBalance()>=cart.getTotal()) {
				cot.setBalance(cot.getBalance()-cart.getTotal());
				checkOutTypeRepository.save(cot);
				cart.setPayed(true);
				cartRepository.save(cart);
				return "Cart successfully paid";
			}
		}
		
	    return "Nothing in Current Cart";
	}
}

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

import com.cashmanager.back.api.model.CheckOutType;
import com.cashmanager.back.api.repository.CheckOutTypeRepository;

@RestController
@RequestMapping("/api")
public class CheckOutTypeController {
	@Autowired
	CheckOutTypeRepository checkOutTypeRepository;
	
	@GetMapping("/checkOutTypes")
	public List<CheckOutType> getAllCheckOutTypes(){
		return checkOutTypeRepository.findAll();
	}
	
	// Create an checkOutType
	@PostMapping("/checkOutTypes")
	public CheckOutType createCheckOutType(@Valid @RequestBody CheckOutType checkOutType) {
	    return checkOutTypeRepository.save(checkOutType);
	}
	
	// Get a Single CheckOutType
	@GetMapping("/checkOutTypes/{id}")
	public CheckOutType getCheckOutTypeById(@PathVariable(value = "id") String checkOutTypeId) throws Exception{
	    return checkOutTypeRepository.findById(checkOutTypeId).get();
	}
	
	// Put a CheckOutType
	@PutMapping("/checkOutTypes/{id}")
	public CheckOutType updateCheckOutType(@PathVariable(value = "id") Long checkOutTypeId, @Valid @RequestBody CheckOutType checkOutTypeDetails) throws Exception{
		CheckOutType checkOutType = checkOutTypeRepository.findById(checkOutTypeId).get();
		checkOutType.setBalance(checkOutTypeDetails.getBalance());
		CheckOutType updatedCheckOutType = checkOutTypeRepository.save(checkOutType);
	    return updatedCheckOutType;
	}
	
	// Delete a CheckOutType
	@DeleteMapping("/checkOutTypes/{id}")
	public ResponseEntity<?> deleteCheckOutType(@PathVariable(value = "id") Long checkOutTypeId) throws Exception{
		checkOutTypeRepository.deleteById(checkOutTypeId);
		return ResponseEntity.ok().build();
	}
}
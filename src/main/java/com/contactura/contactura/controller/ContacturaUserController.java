package com.contactura.contactura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactura.contactura.model.ContacturaUser;
import com.contactura.contactura.repository.ContacturaUserRepository;

@RestController
@RequestMapping({"/user"})

public class ContacturaUserController {
	
	@Autowired
	private ContacturaUserRepository repository;
	
	
	
	@GetMapping
	public List findAll() {
		return repository.findAll();	 
	}
	
	@GetMapping (value = "{id}")
	public ResponseEntity findById(@PathVariable long id) {
		return repository.findById(id)
				.map(user -> ResponseEntity.ok().body(user))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ContacturaUser create (@RequestBody ContacturaUser user) {
		return repository.save(user);
	}
	
	@PutMapping (value = "{id}")
	
	public ResponseEntity update (@PathVariable long id, @RequestBody ContacturaUser contac) {
		return repository.findById(id)
			.map(record -> {
				record.setName(contac.getName());
				record.setUsername(contac.getUsername());
				record.setPassword(contac.getPassword());
				record.setAdmin(false);
				ContacturaUser update = repository.save(record);
				return ResponseEntity.ok().body(update); 
				}).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path = {"/{id}"})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity <?> delete(@PathVariable long id){
		return repository.findById(id)
				.map(record -> {
					repository.deleteById(id);
					return ResponseEntity.ok().build();
		
				}).orElse(ResponseEntity.notFound().build());
	}
	
	private String criptografiaDoPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwordEncripitado = passwordEncoder.encode(password);
		
		return passwordEncripitado;
	}
}
package com.example.demo.service;

import org.springframework.http.ResponseEntity;

public interface IUserBookService {
	ResponseEntity<?> findById(Long user_id);

	ResponseEntity<?> save(Long user_id, Long book_id);

	ResponseEntity<?> deleteById(Long user_id, Long book_id);
}

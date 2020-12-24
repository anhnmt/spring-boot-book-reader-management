package com.example.demo.service;

import com.example.demo.payload.request.BookRequest;
import org.springframework.http.ResponseEntity;

public interface IBookService {
	ResponseEntity<?> findAll();

	ResponseEntity<?> findById(Long id);

	ResponseEntity<?> searchByName(String name);

	ResponseEntity<?> save(BookRequest bookRequest);

	ResponseEntity<?> update(Long id, BookRequest bookRequest);

	ResponseEntity<?> deleteById(Long id);
}

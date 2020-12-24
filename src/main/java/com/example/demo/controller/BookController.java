package com.example.demo.controller;

import com.example.demo.payload.request.BookRequest;
import com.example.demo.service.IBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {
	private final IBookService bookService;

	@GetMapping
	public ResponseEntity<?> findAll() {
		return bookService.findAll();
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody BookRequest bookRequest) {
		return bookService.save(bookRequest);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@Valid @PathVariable Long id) {
		return bookService.findById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @PathVariable Long id,
	                                @Valid @RequestBody BookRequest bookRequest) {
		return bookService.update(id, bookRequest);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@Valid @PathVariable Long id) {
		return bookService.deleteById(id);
	}
}

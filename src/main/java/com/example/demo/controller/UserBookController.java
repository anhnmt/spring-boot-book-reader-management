package com.example.demo.controller;

import com.example.demo.service.IUserBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserBookController {
	private final IUserBookService userBookService;

	@GetMapping("/{user_id}/books")
	public ResponseEntity<?> findById(@Valid @PathVariable Long user_id) {
		return userBookService.findById(user_id);
	}

	@PostMapping("/{user_id}/books/{book_id}")
	public ResponseEntity<?> create(@PathVariable Long user_id, @PathVariable Long book_id) {
		return userBookService.save(user_id, book_id);
	}

	@DeleteMapping("/{user_id}/books/{book_id}")
	public ResponseEntity<?> delete(@PathVariable Long user_id, @PathVariable Long book_id) {
		return userBookService.deleteById(user_id, book_id);
	}

}

package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final IUserService userService;

	@GetMapping
	public ResponseEntity<List<UserEntity>> findAll() {
		return ResponseEntity.ok(userService.findAll());
	}

	@PostMapping
	public ResponseEntity create(@Valid @RequestBody UserEntity userEntity) {
		return ResponseEntity.ok(userService.save(userEntity));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserEntity> findById(@PathVariable Long id) {
		Optional<UserEntity> stock = userService.findById(id);
		if (!stock.isPresent()) {
			log.error("Id " + id + " is not existed");
			ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(stock.get());
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserEntity> update(@PathVariable Long id, @Valid @RequestBody UserEntity userEntity) {
		if (!userService.findById(id).isPresent()) {
			log.error("Id " + id + " is not existed");
			ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(userService.save(userEntity));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Long id) {
		if (!userService.findById(id).isPresent()) {
			log.error("Id " + id + " is not existed");
			ResponseEntity.badRequest().build();
		}

		userService.deleteById(id);

		return ResponseEntity.ok().build();
	}

}

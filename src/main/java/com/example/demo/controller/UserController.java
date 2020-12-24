package com.example.demo.controller;

import com.example.demo.payload.request.UserRequest;
import com.example.demo.service.IAWSService;
import com.example.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final IUserService userService;
	private final IAWSService storageService;

	@Value("${AWS_BUCKET_NAME}")
	private String bucketName;

	@GetMapping
	public ResponseEntity<?> findAll() {
		return userService.findAll();
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody UserRequest userRequest) {
		return userService.save(userRequest);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@Valid @PathVariable Long id) {
		return userService.findById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @PathVariable Long id,
	                                @Valid @RequestBody UserRequest userRequest) {
		return userService.update(id, userRequest);
	}

	@PostMapping(
			path = "/{id}/avatar",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<?> avatar(@PathVariable Long id,
	                                @RequestParam("file") MultipartFile file) {
		return userService.avatar(id, file);
	}

	@PostMapping("/{id}/upload")
	public ResponseEntity<?> upload(@PathVariable Long id,
	                                @RequestBody MultipartFile multipartFile) {
		return null;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@Valid @PathVariable Long id) {
		return userService.deleteById(id);
	}

}

package com.example.demo.service;

import com.example.demo.payload.request.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

public interface IUserService {
	ResponseEntity<?> findAll();

	ResponseEntity<?> findById(Long id);

	ResponseEntity<?> save(UserRequest userRequest);

	ResponseEntity<?> update(Long id, UserRequest userRequest);

	ResponseEntity<?> avatar(Long id, MultipartFile file);

	ResponseEntity<?> upload(MultipartFile file);

	ResponseEntity<?> deleteById(Long id);
}

package com.example.demo.service;

import com.example.demo.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {
	public List<UserEntity> findAll();

	public Optional<UserEntity> findById(Long id);

	public UserEntity save(UserEntity stock);

	public void deleteById(Long id);
}

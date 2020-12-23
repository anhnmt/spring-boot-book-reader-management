package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.payload.request.UserRequest;
import com.example.demo.payload.response.BaseMessage;
import com.example.demo.payload.response.ResponseEntityBO;
import com.example.demo.service.IUserService;
import com.example.demo.util.Common;
import com.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final IUserService userService;

	BaseMessage response;

	@GetMapping
	public ResponseEntity<?> findAll() {
		try {
			List<UserEntity> userEntities = userService.findAll();
			if (Common.isNullOrEmpty(userEntities)) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng nào!", Common.getTimeStamp());
				log.error(Common.createMessageLog(null, response, null, "findAll"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thành công", Common.getTimeStamp(), userEntities);
			log.info(Common.createMessageLog(null, response, null, "findAll"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(null, response, null, "findAll"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody UserRequest userRequest) {
		try {
			UserEntity userEntity = new UserEntity();
			userEntity.setName(userRequest.getName());
			userEntity.setAddress(userRequest.getAddress());
			userEntity.setAge(userRequest.getAge());
			userService.save(userEntity);

			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Cập nhật thành công",
					Common.getTimeStamp(), userEntity);
			log.info(Common.createMessageLog(userRequest, response, null, "create"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(null, response, null, "create"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@Valid @PathVariable Long id) {
		try {
			Optional<UserEntity> userEntity = userService.findById(id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!", Common.getTimeStamp());
				log.error(Common.createMessageLog(id, response, null, "findById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thành công", Common.getTimeStamp(), userEntity);
			log.info(Common.createMessageLog(id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @PathVariable Long id,
	                                @Valid @RequestBody UserRequest userRequest) {
		try {
			Optional<UserEntity> userEntity = userService.findById(id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!", Common.getTimeStamp());
				log.error(Common.createMessageLog(new Object[]{id, userRequest}, response, null, "update"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			userEntity.get().setName(userRequest.getName());
			userEntity.get().setAddress(userRequest.getAddress());
			userEntity.get().setAge(userRequest.getAge());
			userService.save(userEntity.get());

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Cập nhật thành công", Common.getTimeStamp(),
					userEntity);
			log.info(Common.createMessageLog(new Object[]{id, userRequest}, response, null, "update"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(new Object[]{id, userRequest}, response, null, "update"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PostMapping("/{id}/avatar")
	public ResponseEntity<?> avatar(@PathVariable Long id,
	                                @RequestPart(value = "file") MultipartFile file) {
		try {
			Optional<UserEntity> userEntity = userService.findById(id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!", Common.getTimeStamp());
				log.error(Common.createMessageLog(id, response, null, "avatar"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

//			userEntity.get().setAvatar(fileName);
			userService.save(userEntity.get());

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Cập nhật thành công", Common.getTimeStamp(),
					userEntity);
			log.info(Common.createMessageLog(id, response, null, "avatar"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(id, response, null, "avatar"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PostMapping("/{id}/upload")
	public ResponseEntity<?> upload(@PathVariable Long id,
	                                @RequestBody MultipartFile multipartFile) {
		try {
			return null;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@Valid @PathVariable Long id) {
		try {
			if (!userService.existsById(id)) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!", Common.getTimeStamp());
				log.error(Common.createMessageLog(id, response, null, "deleteById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			userService.deleteById(id);
			//tham chiếu đến đối tượng cần trả về
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Xóa Thành công", Common.getTimeStamp());
			log.info(Common.createMessageLog(id, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(id, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

}

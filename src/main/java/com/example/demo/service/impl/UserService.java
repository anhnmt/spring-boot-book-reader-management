package com.example.demo.service.impl;

import com.example.demo.entity.UserEntity;
import com.example.demo.payload.request.UserRequest;
import com.example.demo.payload.response.BaseMessage;
import com.example.demo.payload.response.ResponseEntityBO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IAWSService;
import com.example.demo.service.IUserService;
import com.example.demo.util.Common;
import com.example.demo.util.Constants;
import com.example.demo.util.Excel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements IUserService {
	private final UserRepository userRepository;
	private final IAWSService storageService;
	private BaseMessage response;
	@Value("${AWS_BUCKET_NAME}")
	private String bucketName;

	@Override
	public ResponseEntity<?> findAll() {
		try {
			List<UserEntity> userEntities = userRepository.findAll();

			if (Common.isNullOrEmpty(userEntities)) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng nào!");
				log.error(Common.createMessageLog(null, response, null, "findAll"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thành công", userEntities);
			log.info(Common.createMessageLog(null, response, null, "findAll"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(null, response, null, "findAll"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> findById(Long id) {
		try {
			Optional<UserEntity> userEntity = userRepository.findById(id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!");
				log.error(Common.createMessageLog(id, response, null, "findById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thành công", userEntity);
			log.info(Common.createMessageLog(id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> save(UserRequest userRequest) {
		try {
			if (userRepository.existsByPhone(userRequest.getPhone())) {
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Số điện thoại đã tồn tại");
				log.error(Common.createMessageLog(null, response, null, "save"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			UserEntity userEntity = new UserEntity(
					userRequest.getName(),
					userRequest.getPhone(),
					userRequest.getAddress(),
					userRequest.getAge()
			);

			userRepository.save(userEntity);

			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Thêm thành công");
			log.info(Common.createMessageLog(userRequest, response, null, "save"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(null, response, null, "save"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> update(Long id, UserRequest userRequest) {
		try {
			Optional<UserEntity> userEntity = userRepository.findById(id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!");
				log.error(Common.createMessageLog(new Object[]{id, userRequest}, response, null, "update"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			UserEntity newUserEntity = userEntity.get();

			if (
					userRepository.existsByPhone(userRequest.getPhone())
							&& !newUserEntity.getPhone().equals(userRequest.getPhone())
			) {
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Số điện thoại đã tồn tại");
				log.error(Common.createMessageLog(null, response, null, "save"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			newUserEntity.setName(userRequest.getName());
			newUserEntity.setPhone(userRequest.getPhone());
			newUserEntity.setAddress(userRequest.getAddress());
			newUserEntity.setAge(userRequest.getAge());
			userRepository.save(newUserEntity);

			//tham chiếu đến đối tượng cần trả về
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Cập nhật thành công");
			log.info(Common.createMessageLog(new Object[]{id, userRequest}, response, null, "update"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(new Object[]{id, userRequest}, response, null, "update"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> avatar(Long id, MultipartFile file) {
		try {
			Optional<UserEntity> userEntity = userRepository.findById(id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!");
				log.error(Common.createMessageLog(id, response, null, "avatar"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			String fileName = String.format(
					"avatar-%s.%s",
					id,
					FilenameUtils.getExtension(file.getOriginalFilename()));

			storageService.save(bucketName, fileName, storageService.extractMetadata(file), file.getInputStream());

			UserEntity newUserEntity = userEntity.get();
			newUserEntity.setAvatar(storageService.download(bucketName, fileName));
			userRepository.save(newUserEntity);

			//tham chiếu đến đối tượng cần trả về
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Cập nhật avatar thành công");
			log.info(Common.createMessageLog(id, response, null, "avatar"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, "Cập nhật avatar không thành công",
					Common.getTimeStamp());
			log.error(Common.createMessageLog(id, response, null, "avatar"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> upload(MultipartFile file) {
		try {
			List<UserEntity> userEntities = Excel.readSheetToUsers(file.getInputStream());

			if (Common.isNullOrEmpty(userEntities)) {
				response = new BaseMessage(Constants.ERROR_RESPONSE, "File không có người dùng",
						Common.getTimeStamp());
				log.error(Common.createMessageLog(file.getOriginalFilename(), response, null, "upload"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			userRepository.saveAll(userEntities);

			//tham chiếu đến đối tượng cần trả về
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Upload người dùng thành công");
			log.info(Common.createMessageLog(file.getOriginalFilename(), response, null, "upload"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, "Upload không thành công",
					Common.getTimeStamp());
			log.error(Common.createMessageLog(file.getOriginalFilename(), response, null, "upload"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> deleteById(Long id) {
		try {
			if (!userRepository.existsById(id)) {
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!");
				log.error(Common.createMessageLog(id, response, null, "deleteById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			userRepository.deleteById(id);
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Xoá thành công");
			log.info(Common.createMessageLog(id, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, "Xoá người dùng không thành công",
					Common.getTimeStamp());
			log.error(Common.createMessageLog(id, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
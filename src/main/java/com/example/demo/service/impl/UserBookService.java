package com.example.demo.service.impl;

import com.example.demo.entity.BookEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.payload.response.BaseMessage;
import com.example.demo.payload.response.ResponseEntityBO;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.IUserBookService;
import com.example.demo.util.Common;
import com.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserBookService implements IUserBookService {
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private BaseMessage response;

	@Override
	public ResponseEntity<?> findById(Long user_id) {
		try {
			Optional<UserEntity> userEntity = userRepository.findById(user_id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!");
				log.error(Common.createMessageLog(user_id, response, null, "findById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			Set<BookEntity> bookEntities = userEntity.get().getBookEntities();

			System.out.println(bookEntities);

			if (Common.isNullOrEmpty(bookEntities)) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Người dùng chưa đọc sách nào!");
				log.error(Common.createMessageLog(null, response, null, "findById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thành công", bookEntities);
			log.info(Common.createMessageLog(user_id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(user_id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> save(Long user_id, Long book_id) {
		try {
			Optional<UserEntity> userEntity = userRepository.findById(user_id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!");
				log.error(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "save"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			Optional<BookEntity> bookEntity = bookRepository.findById(book_id);

			if (!bookEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có sách này!");
				log.error(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "save"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			UserEntity newUserEntity = userEntity.get();

			Set<BookEntity> bookEntities = newUserEntity.getBookEntities();
			bookEntities.add(bookEntity.get());

			newUserEntity.setBookEntities(bookEntities);

			userRepository.save(newUserEntity);

			//tham chiếu đến đối tượng cần trả về
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Thêm thành công");
			log.info(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> deleteById(Long user_id, Long book_id) {
		try {
			Optional<UserEntity> userEntity = userRepository.findById(user_id);

			if (!userEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có người dùng này!");
				log.error(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "deleteById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			if (!bookRepository.existsById(book_id)) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có sách này!");
				log.error(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "deleteById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			UserEntity newUserEntity = userEntity.get();

			Set<BookEntity> bookEntities = newUserEntity.getBookEntities();

			bookEntities.forEach(book -> {
				if (book.getId().equals(book_id)) {
					bookEntities.remove(book);
				}
			});

			newUserEntity.setBookEntities(bookEntities);

			userRepository.save(newUserEntity);

			//tham chiếu đến đối tượng cần trả về
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Xoá thành công");
			log.info(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(new Object[]{user_id, book_id}, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
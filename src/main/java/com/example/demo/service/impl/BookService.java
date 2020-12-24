package com.example.demo.service.impl;

import com.example.demo.entity.BookEntity;
import com.example.demo.payload.request.BookRequest;
import com.example.demo.payload.response.BaseMessage;
import com.example.demo.payload.response.ResponseEntityBO;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.IBookService;
import com.example.demo.util.Common;
import com.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookService implements IBookService {
	@Autowired
	private BookRepository bookRepository;

	private BaseMessage response;

	@Override
	public ResponseEntity<?> findAll() {
		try {
			List<BookEntity> bookEntities = bookRepository.findAll();

			if (Common.isNullOrEmpty(bookEntities)) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có cuốn sách nào!", Common.getTimeStamp());
				log.error(Common.createMessageLog(null, response, null, "findAll"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thành công", Common.getTimeStamp(), bookEntities);
			log.info(Common.createMessageLog(null, response, null, "findAll"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(null, response, null, "findAll"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}


	@Override
	public ResponseEntity<?> findById(Long id) {
		try {
			Optional<BookEntity> bookEntity = bookRepository.findById(id);

			if (!bookEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có cuốn sách này!", Common.getTimeStamp());
				log.error(Common.createMessageLog(id, response, null, "findById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thành công", Common.getTimeStamp(), bookEntity);
			log.info(Common.createMessageLog(id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(id, response, null, "findById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> save(BookRequest bookRequest) {
		try {
			if (bookRepository.existsByName(bookRequest.getName())) {
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Sách đã tồn tại", Common.getTimeStamp());
				log.error(Common.createMessageLog(null, response, null, "save"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			BookEntity bookEntity = new BookEntity(
					bookRequest.getName(),
					bookRequest.getAuthor(),
					bookRequest.getPrice()
			);

			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Thêm thành công",
					Common.getTimeStamp(), bookRepository.save(bookEntity));
			log.info(Common.createMessageLog(bookRequest, response, null, "save"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(null, response, null, "save"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> update(Long id, BookRequest bookRequest) {
		try {
			Optional<BookEntity> bookEntity = bookRepository.findById(id);

			if (!bookEntity.isPresent()) {
				//sử dựng hàm khởi tạo để giúp code ngắn gọn hơn
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có cuốn sách này!", Common.getTimeStamp());
				log.error(Common.createMessageLog(new Object[]{id, bookRequest}, response, null, "update"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			BookEntity newBookEntity = bookEntity.get();

			if (
					bookRepository.existsByName(bookRequest.getName())
							&& !newBookEntity.getName().equals(bookRequest.getName())
			) {
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Sách đã tồn tại", Common.getTimeStamp());
				log.error(Common.createMessageLog(null, response, null, "save"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			newBookEntity.setName(bookRequest.getName());
			newBookEntity.setAuthor(bookRequest.getAuthor());
			newBookEntity.setPrice(bookRequest.getPrice());
			bookRepository.save(newBookEntity);

			//tham chiếu đến đối tượng cần trả về
			response = new ResponseEntityBO<>(Constants.SUCCESS_RESPONSE, "Cập nhật thành công",
					Common.getTimeStamp(), newBookEntity);
			log.info(Common.createMessageLog(new Object[]{id, bookRequest}, response, null, "update"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage(), Common.getTimeStamp());
			log.error(Common.createMessageLog(new Object[]{id, bookRequest}, response, null, "update"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@Override
	public ResponseEntity<?> deleteById(Long id) {
		try {
			if (!bookRepository.existsById(id)) {
				response = new BaseMessage(Constants.ERROR_RESPONSE, "Không có cuốn sách này!", Common.getTimeStamp());
				log.error(Common.createMessageLog(id, response, null, "deleteById"));

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			bookRepository.deleteById(id);
			response = new BaseMessage(Constants.SUCCESS_RESPONSE, "Xoá thành công", Common.getTimeStamp());
			log.info(Common.createMessageLog(id, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, "Xoá cuốn sách không thành công",
					Common.getTimeStamp());
			log.error(Common.createMessageLog(id, response, null, "deleteById"));

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
package com.example.demo.util;

import com.example.demo.entity.BookEntity;
import com.example.demo.payload.response.BaseMessage;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Seeder implements CommandLineRunner {
	private final BookRepository bookRepository;

	@Override
	public void run(String... args) throws Exception {
		BaseMessage response;

		try {
			if (bookRepository.count() <= 0) {
				bookRepository.save(new BookEntity("Truyện Kiều", "Nguyễn Du", 20000.0));
				bookRepository.save(new BookEntity("Chí Phèo", "Nam Cao", 10000.0));
				bookRepository.save(new BookEntity("Lão Hạc", "Nam Cao", 18000.0));
				bookRepository.save(new BookEntity("Tắt Đèn", "Ngô Tất Tố", 15000.0));
				bookRepository.save(new BookEntity("Vợ Nhặt", "Kim Lân", 8000.0));
				bookRepository.save(new BookEntity("Số Đỏ", "Vũ Trọng Phụng", 25000.0));

				response = new BaseMessage(Constants.SUCCESS_RESPONSE, "BookSeeder success");
				log.error(Common.createMessageLog(null, response, null, "run"));
			}
		} catch (Exception e) {
			response = new BaseMessage(Constants.ERROR_RESPONSE, e.getMessage());
			log.error(Common.createMessageLog(null, response, null, "run"));
		}
	}
}

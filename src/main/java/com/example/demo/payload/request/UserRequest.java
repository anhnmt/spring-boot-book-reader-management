package com.example.demo.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserRequest {
	@NotBlank
	@NotNull
	@Size(min = 8, max = 200)
	private String name;

	@NotBlank
	@NotNull
	@Size(min = 9, max = 10)
	private String phone;

	private String address;

	@Min(8)
	@Max(200)
	private Integer age;
}

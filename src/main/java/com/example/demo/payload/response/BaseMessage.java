package com.example.demo.payload.response;

import com.example.demo.util.Common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage {
	private Boolean status; // 0 - thanh cong, 1 - that bai
	private String message; // mo ta loi
	private long timestamp = Common.getTimeStamp();

	public BaseMessage(Boolean status, String message) {
		this.status = status;
		this.message = message;
	}
}

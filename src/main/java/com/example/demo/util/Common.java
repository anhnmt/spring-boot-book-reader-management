package com.example.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Common {
	public static Long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof String)
			return obj.toString().isEmpty();

		if (obj instanceof List)
			return ((List) obj).isEmpty();

		if (obj instanceof Set)
			return ((Set) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		return false;
	}

	public static String createMessageLog(Object input, Object response, String userName, String methodName) {
		StringBuilder sb = new StringBuilder();
		Gson gson = new Gson();
		ObjectMapper objectMapper = new ObjectMapper();
		sb.append(getTimeStamp());

		try {
			if (!isNullOrEmpty(userName)) {
				sb.append("_").append(userName);
			}
			if (!isNullOrEmpty(input))
				sb.append("_").append(objectMapper.writeValueAsString(input));
			if (!isNullOrEmpty(methodName))
				sb.append("_").append(gson.toJson(methodName));
			if (!isNullOrEmpty(response)) {

				sb.append("_").append(objectMapper.writeValueAsString(response));
			}
		} catch (Exception e) {
			e.getMessage();
		}

		return sb.toString();
	}
}

package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface IAWSService {
	public void save(String path,
	                 String fileName,
	                 Optional<Map<String, String>> optionalMetaData,
	                 InputStream inputStream);

	public String download(String path, String fileName);

	public Optional<Map<String, String>> extractMetadata(MultipartFile file);
}

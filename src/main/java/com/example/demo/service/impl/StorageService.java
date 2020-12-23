package com.example.demo.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.service.IStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {
	private final AmazonS3 amazonS3;

	@Override
	public void save(String path,
	                 String fileName,
	                 Optional<Map<String, String>> optionalMetaData,
	                 InputStream inputStream) {
		ObjectMetadata metaData = new ObjectMetadata();
		optionalMetaData.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(metaData::addUserMetadata);
			}
		});

		try {
			PutObjectRequest request = new PutObjectRequest(path, fileName, inputStream, metaData);
			request.setCannedAcl(CannedAccessControlList.PublicRead);
			amazonS3.putObject(request);
		} catch (AmazonServiceException e) {
			throw new IllegalStateException("Failed to store file to s3", e);
		}
	}

	@Override
	public String download(String path, String fileName) {
		try {
			URL url = amazonS3.getUrl(path, fileName);

			return url.toExternalForm();
		} catch (AmazonServiceException e) {
			throw new IllegalStateException("Failed to download file to S3", e);
		}
	}

	@Override
	public Optional<Map<String, String>> extractMetadata(MultipartFile file) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));

		return Optional.of(metadata);
	}
}

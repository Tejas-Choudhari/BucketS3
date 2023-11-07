package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ServiceClass;

@RestController()
@RequestMapping("/s3")
public class ControllerS3 {

	@Autowired
	private ServiceClass serviceClass;

	@PostMapping(value = "/upload")
	public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
		return new ResponseEntity<>(serviceClass.uploadFile(file), HttpStatus.OK);
	}

	@GetMapping(path = "/download/{filename}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String filename) {
		byte[] data = serviceClass.downloadFile(filename);
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity.ok()
				.contentLength(data.length)
				.header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + filename + "\"")
				.body(resource);
	}

	@DeleteMapping(path = "/delete/{filename}")
	public ResponseEntity<String> deleteFile(@PathVariable String filename) {
		if (filename == null || filename.isEmpty()) {
			return new ResponseEntity<>("Invalid filename provided. File was not deleted.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(serviceClass.deleteFile(filename), HttpStatus.OK);
	}

}

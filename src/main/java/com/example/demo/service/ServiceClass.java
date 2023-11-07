package com.example.demo.service;

import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceClass {

	@Value("${application.bucket.name}")
	private String bucketName;

	@Autowired
	private AmazonS3 s3client;

	// uppload file by using put
	public String uploadFile(MultipartFile file) {
		File fileConversion = convertFileToFile(file);

		String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		s3client.putObject(bucketName, filename, fileConversion);
		fileConversion.delete();

		return " File is uploaded " + filename;
	}

	// method to convert the multiple file to one file
	private File convertFileToFile(MultipartFile file) {

		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException error) {
			log.error("Error - File not Converted", error);
		}
		return convertedFile;
	}

	// for download the file
	// the format is in binary so we use byte[]
	public byte[] downloadFile(String filename) {

		S3Object s3Object = s3client.getObject(bucketName, filename);
		S3ObjectInputStream inputstream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputstream);
			return content;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// for deleting the file

	public String deleteFile(String filename) {
		s3client.deleteObject(bucketName, filename);
		return filename + " File Deleted Successfully";
	}

}

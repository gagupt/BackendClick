package com.example.demo.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  boolean uploadVideo(MultipartFile file);

  List<String> listObjects();

  boolean deleteKeys(List<String> keys, String phoneNo);
}

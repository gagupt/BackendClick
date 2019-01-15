package com.example.demo.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.data.User;

public interface MessageService {

  boolean uploadImage(MultipartFile file, String phoneNo);

  List<String> listObjects();

  boolean deleteKeys(List<String> keys, String phoneNo);

  List<String> getMyUploads(String phoneno);

  User getUser(String phoneNo);

  boolean createUser(String mobileNo, String name);
}

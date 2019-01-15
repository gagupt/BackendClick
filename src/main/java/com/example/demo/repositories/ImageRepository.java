package com.example.demo.repositories;

import java.util.Collection;
import java.util.List;
import com.example.demo.data.ImageUser;

public interface ImageRepository {
  List<String> getImages(String phoneNo);

  boolean createImageUser(ImageUser imageUser);

  boolean deleteImages(Collection<String> keys, String phoneNo);
}

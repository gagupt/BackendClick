package com.example.demo.repositories;

import java.util.List;
import com.example.demo.data.Keys;

public interface MessageRepository {

  boolean update(Keys keys);

  List<String> getKeys(String id);

  // List<Keys> getLastHourMessages();
}

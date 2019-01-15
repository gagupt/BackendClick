package com.example.demo.repositories;

import com.example.demo.data.User;

public interface UserRepository {
  User getUser(String phoneNo);

  boolean createUser(User user);
}

package com.example.demo.services;

import com.example.demo.entities.models.UserRequest;

public interface DemoService {

	String hello();

	UserRequest getUser(int id);

	int getUserId(int id, String name);
}

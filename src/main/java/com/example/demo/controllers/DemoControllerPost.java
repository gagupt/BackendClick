package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.models.UserRequest;
import com.example.demo.entities.models.UserRequest;
import com.example.demo.services.DemoService;

@RestController
public class DemoControllerPost {
	private final DemoService demoService;

	@Autowired
	public DemoControllerPost(DemoService demoService) {
		this.demoService = demoService;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/updateUser")
	public @ResponseBody UserRequest getDemo(@RequestBody UserRequest userRequest) {
		int id = userRequest.getId();
		String name = userRequest.getName();
		UserRequest UserRequest = new UserRequest();
		UserRequest.setId(userRequest.getId());
		UserRequest.setName(userRequest.getName());
		UserRequest.setId(demoService.getUserId(id, name));
		return UserRequest;
	}
}

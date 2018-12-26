package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.models.UserRequest;
import com.example.demo.services.DemoService;

@RestController
public class DemoController {
	private final DemoService demoService;

	@Autowired
	public DemoController(DemoService demoService) {
		this.demoService = demoService;
	}

	@RequestMapping(value = "getUser", method = RequestMethod.GET)
	public @ResponseBody UserRequest getDemo(
			@RequestParam(value = "id", required = false, defaultValue = "Default response") int id) {
		return demoService.getUser(id);
	}

	@RequestMapping(value = "hello", method = RequestMethod.GET)
	public @ResponseBody String hello(
			@RequestParam(value = "id", required = false, defaultValue = "Default response") int id) {
		return demoService.hello();
	}
}

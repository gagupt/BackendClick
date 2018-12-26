package com.example.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.data.MessageObj;
import com.example.demo.services.MessageService;

@RestController
public class MessageController {
	private final MessageService messageService;

	@Autowired
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@RequestMapping(value = "createMessage", method = RequestMethod.POST)
	public boolean createMessage(@RequestBody MessageObj messageObj) {
		return messageService.createMessage(messageObj);
	}

	@RequestMapping(value = "getMessage", method = RequestMethod.GET)
	public @ResponseBody MessageObj getMessage(@RequestParam(value = "messageId") String messageId) {
		return messageService.getMessage(messageId);
	}

	@RequestMapping(value = "getLastHourMessages", method = RequestMethod.GET)
	public @ResponseBody List<MessageObj> getLastHourMessages() {
		return messageService.getLastHourMessages();
	}

	@RequestMapping(value = "upload/image", method = RequestMethod.POST)
	public @ResponseBody boolean uploadVideo(@RequestParam MultipartFile file) {
		return messageService.uploadVideo(file);

	}
}

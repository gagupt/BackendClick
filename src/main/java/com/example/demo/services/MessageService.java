package com.example.demo.services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.data.MessageObj;

public interface MessageService {

	boolean createMessage(MessageObj message);

	MessageObj getMessage(String messageId);

	List<MessageObj> getLastHourMessages();

	boolean uploadVideo(MultipartFile file);
}

package com.example.demo.repositories;

import java.util.List;
import com.example.demo.data.MessageObj;

public interface MessageRepository {

	boolean createMessage(MessageObj message);

	MessageObj getMessage(String messageId);

	List<MessageObj> getLastHourMessages();
}

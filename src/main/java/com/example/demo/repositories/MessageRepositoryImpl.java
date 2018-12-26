package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Page;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.example.demo.data.MessageObj;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

	private final DynamoDB dynamoClient;
	private static final String TABLE_NAME = "MessageTable";
	private static final String MESSAGE_ID = "MessageId";
	private static final String TEXT = "Text";
	private static final String TIMESTAMP = "Timestamp";
	private static final String HASH_KEY_NAME = MESSAGE_ID;

	@Autowired
	public MessageRepositoryImpl(DynamoDB dynamoClient) {
		this.dynamoClient = dynamoClient;
	}

	@Override
	public boolean createMessage(MessageObj message) {
		boolean success = false;
		Table table = dynamoClient.getTable(TABLE_NAME);
		Item item = new Item();
		item.withPrimaryKey(HASH_KEY_NAME, message.getMessageId()).withString(TEXT, message.getText())
				.withLong(TIMESTAMP, System.currentTimeMillis());

		PutItemOutcome outcome = table.putItem(item);
		int statusCode = outcome.getPutItemResult().getSdkHttpMetadata().getHttpStatusCode();
		success = statusCode == HttpStatus.SC_OK;
		return success;
	}

	@Override
	public MessageObj getMessage(String messageId) {
		Table table = dynamoClient.getTable(TABLE_NAME);
		Item item = table.getItem(HASH_KEY_NAME, messageId);
		return getMessage(item);
	}

	private MessageObj getMessage(Item item) {
		MessageObj messageObj = new MessageObj();
		if (item != null) {
			messageObj.setMessageId(item.getString(MESSAGE_ID));
			messageObj.setText(item.getString(TEXT));
			messageObj.setSendAt(item.getLong(TIMESTAMP));
		}
		return messageObj;
	}

	@Override
	public List<MessageObj> getLastHourMessages() {
		NameMap nameMap = new NameMap().with("#TIME", TIMESTAMP);
		ValueMap valueMap = new ValueMap().withNumber(":from", System.currentTimeMillis() - 10 * 60 * 1000);
		ScanSpec scanSpec = new ScanSpec().withConsistentRead(true).withFilterExpression("#TIME >= :from")
				.withMaxResultSize(50).withNameMap(nameMap).withValueMap(valueMap);

		Page<Item, ScanOutcome> page = dynamoClient.getTable(TABLE_NAME).scan(scanSpec).firstPage();
		List<MessageObj> messages = new ArrayList<>();
		for (Item item : page) {
			MessageObj messageObj = getMessage(item);
			messages.add(messageObj);
		}
		return messages;
	}
}

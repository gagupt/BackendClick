package com.example.demo.repositories;

import java.util.List;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.example.demo.data.Keys;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

  private final DynamoDB dynamoClient;
  private static final String TABLE_NAME = "Keys";
  private static final String ID = "Id";
  private static final String KEYS = "Keys";
  private static final String HASH_KEY_NAME = ID;

  @Autowired
  public MessageRepositoryImpl(DynamoDB dynamoClient) {
    this.dynamoClient = dynamoClient;
  }

  @Override
  public boolean update(Keys keys) {
    boolean success = false;
    Table table = dynamoClient.getTable(TABLE_NAME);
    Item item = new Item();
    item.withPrimaryKey(HASH_KEY_NAME, keys.getId()).withList(KEYS, keys.getKeys());

    PutItemOutcome outcome = table.putItem(item);
    int statusCode = outcome.getPutItemResult().getSdkHttpMetadata().getHttpStatusCode();
    success = statusCode == HttpStatus.SC_OK;
    return success;
  }

  @Override
  public List<String> getKeys(String id) {
    List<String> keys = null;
    Table table = dynamoClient.getTable(TABLE_NAME);
    Item item = table.getItem(HASH_KEY_NAME, id);
    if (item != null) {
      keys = item.getList(KEYS);;
    }
    return keys;
  }

  // @Override
  // public List<Keys> getLastHourMessages() {
  // NameMap nameMap = new NameMap().with("#TIME", TIMESTAMP);
  // ValueMap valueMap = new ValueMap().withNumber(":from", System.currentTimeMillis() - 10 * 60 *
  // 1000);
  // ScanSpec scanSpec = new ScanSpec().withConsistentRead(true).withFilterExpression("#TIME >=
  // :from")
  // .withMaxResultSize(50).withNameMap(nameMap).withValueMap(valueMap);
  //
  // Page<Item, ScanOutcome> page = dynamoClient.getTable(TABLE_NAME).scan(scanSpec).firstPage();
  // List<Keys> messages = new ArrayList<>();
  // for (Item item : page) {
  // Keys messageObj = getMessage(item);
  // messages.add(messageObj);
  // }
  // return messages;
  // }
}

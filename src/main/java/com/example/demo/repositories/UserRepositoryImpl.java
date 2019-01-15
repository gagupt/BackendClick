package com.example.demo.repositories;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.example.demo.data.User;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final DynamoDB dynamoClient;
  private static final String TABLE_NAME = "UserTable";
  private static final String ID = "PhoneNo";
  private static final String NAME = "Name";
  private static final String HASH_KEY_NAME = ID;

  @Autowired
  public UserRepositoryImpl(DynamoDB dynamoClient) {
    this.dynamoClient = dynamoClient;
  }

  User getUser(Item item) {
    User user = new User();
    if (item != null) {
      user.setMobileNo(item.getString(HASH_KEY_NAME));
      user.setName(item.getString(NAME));
    }
    return user;
  }

  @Override
  public User getUser(String phoneNo) {
    Table table = dynamoClient.getTable(TABLE_NAME);
    Item item = table.getItem(HASH_KEY_NAME, phoneNo);
    return getUser(item);
  }

  @Override
  public boolean createUser(User user) {
    boolean success = false;
    Table table = dynamoClient.getTable(TABLE_NAME);
    Item item = new Item();
    item.withPrimaryKey(HASH_KEY_NAME, user.getMobileNo()).withString(NAME, user.getName());

    PutItemOutcome outcome = table.putItem(item);
    int statusCode = outcome.getPutItemResult().getSdkHttpMetadata().getHttpStatusCode();
    success = statusCode == HttpStatus.SC_OK;
    return success;
  }
}

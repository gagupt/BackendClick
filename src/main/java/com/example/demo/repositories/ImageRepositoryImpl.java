package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.amazonaws.http.SdkHttpMetadata;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.example.demo.data.ImageUser;

@Repository
public class ImageRepositoryImpl implements ImageRepository {

  private final DynamoDB dynamoClient;
  private static final String TABLE_NAME = "UserImages";
  private static final String INDEX_NAME = "PhoneNo-index";
  private static final String KEY = "Key";
  private static final String PHONE_NO = "PhoneNo";
  private static final String HASH_KEY_NAME = KEY;
  private static final String RANGE_KEY_NAME = PHONE_NO;

  @Autowired
  public ImageRepositoryImpl(DynamoDB dynamoClient) {
    this.dynamoClient = dynamoClient;
  }

  ImageUser getImageUser(Item item) {
    ImageUser imageUser = new ImageUser();
    if (item != null) {
      imageUser.setKey(item.getString(HASH_KEY_NAME));
      imageUser.setPhoneNo(item.getString(PHONE_NO));
    }
    return imageUser;
  }

  @Override
  public List<String> getImages(String phoneNo) {
    List<String> keys = new ArrayList<>();
    Table table = dynamoClient.getTable(TABLE_NAME);
    Index index = table.getIndex(INDEX_NAME);
    ItemCollection<QueryOutcome> items;

    QuerySpec spec = new QuerySpec().withKeyConditionExpression("#phone = :phoneNo")
        .withNameMap(new NameMap().with("#phone", RANGE_KEY_NAME))
        .withValueMap(new ValueMap().withString(":phoneNo", phoneNo));
    items = index.query(spec);
    for (Item item : items) {
      keys.add(getImageUser(item).getKey());
    }
    return keys;
  }

  @Override
  public boolean deleteImages(Collection<String> keys, String phoneNo) {
    boolean success = false;
    Table table = dynamoClient.getTable(TABLE_NAME);
    for (String key : keys) {
      DeleteItemOutcome deleteItemOutcome =
          table.deleteItem(new PrimaryKey(HASH_KEY_NAME, key, RANGE_KEY_NAME, phoneNo));
      SdkHttpMetadata httpMetadata = deleteItemOutcome.getDeleteItemResult().getSdkHttpMetadata();
      success = success && httpMetadata.getHttpStatusCode() == HttpStatus.SC_OK;
    }
    return success;
  }

  @Override
  public boolean createImageUser(ImageUser imageUser) {
    boolean success = false;
    Table table = dynamoClient.getTable(TABLE_NAME);
    Item item = new Item();
    item.withPrimaryKey(HASH_KEY_NAME, imageUser.getKey()).withString(RANGE_KEY_NAME,
        imageUser.getPhoneNo());

    PutItemOutcome outcome = table.putItem(item);
    int statusCode = outcome.getPutItemResult().getSdkHttpMetadata().getHttpStatusCode();
    success = statusCode == HttpStatus.SC_OK;
    return success;
  }
}

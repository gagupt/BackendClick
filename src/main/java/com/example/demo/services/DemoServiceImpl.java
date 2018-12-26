package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.example.demo.entities.models.UserRequest;
import com.example.demo.entities.models.UserRequest;

@Service
public class DemoServiceImpl implements DemoService {
	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	private DynamoDBMapper dynamoDBMapper;

	@Override
	public UserRequest getUser(int id) {

		UserRequest UserRequest = new UserRequest();

		UserRequest.setId(id);
		DynamoDBQueryExpression<UserRequest> queryExpression = new DynamoDBQueryExpression<UserRequest>()
				.withHashKeyValues(UserRequest);

		List<UserRequest> itemList = dynamoDBMapper.query(UserRequest.class, queryExpression);

		for (int i = 0; i < itemList.size(); i++) {
			UserRequest.setName((itemList.get(i).getName()));
		}
		return UserRequest;
	}

	@Override
	public int getUserId(int id, String name) {

		dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

		UserRequest userRequest = new UserRequest();
		userRequest.setId(id);
		userRequest.setName(name);
		dynamoDBMapper.save(userRequest);
		return id;
	}

	@Override
	public String hello() {
		return "Docker test..";
	}

}

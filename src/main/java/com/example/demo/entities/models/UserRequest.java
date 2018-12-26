package com.example.demo.entities.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "User")
public class UserRequest {
	private int id;
	private String name;

	@DynamoDBHashKey(attributeName = "UserId")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "UserName")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

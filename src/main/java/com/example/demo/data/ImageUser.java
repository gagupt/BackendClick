package com.example.demo.data;

public class ImageUser {
  private String key;
  private String phoneNo;



  public String getKey() {
    return key;
  }


  public void setKey(String key) {
    this.key = key;
  }


  public String getPhoneNo() {
    return phoneNo;
  }


  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }


  public ImageUser(String key, String phoneNo) {
    super();
    this.key = key;
    this.phoneNo = phoneNo;
  }


  public ImageUser() {
    super();
  }

}

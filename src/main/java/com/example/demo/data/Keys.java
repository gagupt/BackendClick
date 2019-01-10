package com.example.demo.data;

import java.util.List;

public class Keys {
  private String id;
  private List<String> keys;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getKeys() {
    return keys;
  }

  public void setKeys(List<String> keys) {
    this.keys = keys;
  }

  public Keys(String id, List<String> keys) {
    super();
    this.id = id;
    this.keys = keys;
  }

}

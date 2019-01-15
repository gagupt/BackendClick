package com.example.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.data.User;
import com.example.demo.services.MessageService;

@RestController
public class MessageController {
  private final MessageService messageService;

  @Autowired
  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  // @RequestMapping(value = "createMessage", method = RequestMethod.POST)
  // public boolean createMessage(@RequestBody Keys messageObj) {
  // return messageService.updateKeys(messageObj);
  // }
  //
  // @RequestMapping(value = "getMessage", method = RequestMethod.GET)
  // public @ResponseBody Keys getMessage(@RequestParam(value = "messageId") String messageId) {
  // return messageService.getKeys(messageId);
  // }
  //
  // @RequestMapping(value = "getLastHourMessages", method = RequestMethod.GET)
  // public @ResponseBody List<Keys> getLastHourMessages() {
  // return messageService.getLastHourMessages();
  // }

  @RequestMapping(value = "upload/image", method = RequestMethod.POST)
  public boolean uploadVideo(@RequestParam MultipartFile file, String phoneNo) {
    return messageService.uploadImage(file, phoneNo);
  }

  @RequestMapping(value = "delete/images", method = RequestMethod.POST)
  public boolean deleteImage(@RequestParam List<String> keys, String phoneNo) {
    return messageService.deleteKeys(keys, phoneNo);
  }

  @RequestMapping(value = "download/image", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<String> listObjects() {
    return messageService.listObjects();
  }


  @RequestMapping(value = "get/user/image", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<String> getUserImage(@RequestParam String phoneNo) {
    return messageService.getMyUploads(phoneNo);
  }

  @RequestMapping(value = "get/user", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public User getUser(@RequestParam String phoneNo) {
    return messageService.getUser(phoneNo);
  }

  @RequestMapping(value = "create/user", method = RequestMethod.POST)
  public boolean createUser(@RequestBody User user) {
    return messageService.createUser(user);
  }
}

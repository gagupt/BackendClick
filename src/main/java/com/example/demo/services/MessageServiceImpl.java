package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.CollectionUtils;
import com.amazonaws.util.IOUtils;
import com.example.demo.data.ImageUser;
import com.example.demo.data.Keys;
import com.example.demo.data.User;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.UserRepository;

@Service
public class MessageServiceImpl implements MessageService {

  private final String KEY_ID = "KeyId";
  private final MessageRepository messageRepo;
  private final UserRepository userRepository;
  private final ImageRepository imageRepository;
  private final AmazonS3 s3Client;

  @Autowired
  public MessageServiceImpl(MessageRepository messageRepo, AmazonS3 s3Client,
      UserRepository userRepository, ImageRepository imageRepository) {
    this.messageRepo = messageRepo;
    this.s3Client = s3Client;
    this.userRepository = userRepository;
    this.imageRepository = imageRepository;
  }

  @Override
  public boolean uploadImage(MultipartFile file, String phoneNo) {
    try {
      InputStream is = file.getInputStream();
      ObjectMetadata meta = new ObjectMetadata();
      byte[] bytes = IOUtils.toByteArray(is);

      meta.setContentLength(bytes.length);
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

      String key = "image" + String.valueOf(System.currentTimeMillis()) + ".jpg";
      List<String> keys = messageRepo.getKeys(KEY_ID);
      if (CollectionUtils.isNullOrEmpty(keys)) {
        keys = new ArrayList<>();
      }
      keys.add(key);

      messageRepo.update(new Keys(KEY_ID, keys));

      imageRepository.createImageUser(new ImageUser(key, phoneNo));

      String bucket = "my-bucket-images";
      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucket, key, byteArrayInputStream, meta);
      putObjectRequest.setMetadata(meta);
      s3Client.putObject(putObjectRequest);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return true;

  }

  @Override
  public List<String> listObjects() {

    return messageRepo.getKeys(KEY_ID);
    // // List<byte[]> listImages = new ArrayList<>();
    // String bucketName = "my-bucket-images";
    // List<String> urls = new ArrayList<>();
    // ListObjectsRequest listObjectsRequest =
    // new ListObjectsRequest().withBucketName(bucketName).withPrefix("image");
    // ObjectListing objectListing;
    // do {
    // objectListing = s3Client.listObjects(listObjectsRequest);
    //
    // for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
    // // System.out.println("object" + objectSummary.toString() + "=" + " - "
    // // + objectSummary.getKey() + " " + "(size = " + objectSummary.getSize() + ")");
    // URL url = s3Client.getUrl(bucketName, objectSummary.getKey());
    // urls.add(url.toString());
    //
    // // System.out.println("count=" + count);
    // // count++;
    // // System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
    // // System.out.println("getObjectMetadata: " + fullObject.getObjectMetadata());
    // // System.out.println("Content: " + fullObject.getObjectContent());
    // }
    // // listObjectsRequest.setMarker(objectListing.getNextMarker());
    // } while (objectListing.isTruncated());
    // return urls;
  }

  @Override
  public boolean deleteKeys(List<String> keys, String phoneNo) {
    boolean success1 = false;
    boolean success2 = false;
    List<String> userKeys = imageRepository.getImages(phoneNo);
    List<String> keysToDelete = new ArrayList<>();

    for (String key : keys) {
      if (userKeys.contains(key)) {
        keysToDelete.add(key);
      }
    }
    if (keysToDelete.size() > 0) {
      success1 = imageRepository.deleteImages(keysToDelete, phoneNo);
    }

    List<String> oldkeys = messageRepo.getKeys(KEY_ID);
    List<String> newKeys = new ArrayList<>();

//    System.out.println("oldkeys=" + oldkeys.size());

    for (String oldkey : oldkeys) {
      if (!keysToDelete.contains(oldkey)) {
        newKeys.add(oldkey);
      }
    }

//    System.out.println("newKeys=" + newKeys.size());


//    for (String kk : keysToDelete) {
//      System.out.println("keysToDelete" + kk);
//    }

    success2 = messageRepo.update(new Keys(KEY_ID, newKeys));

//    System.out.println("success1=" + success1);
//    System.out.println("success2=" + success2);
    return success1 && success2;
  }

  @Override
  public List<String> getMyUploads(String phoneNo) {
    return imageRepository.getImages(phoneNo);
  }

  @Override
  public User getUser(String phoneNo) {
    return userRepository.getUser(phoneNo);
  }

  @Override
  public boolean createUser(String mobileNo, String name) {
    return userRepository.createUser(mobileNo, name);
  }
}

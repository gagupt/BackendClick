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
import com.example.demo.data.Keys;
import com.example.demo.repositories.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {

  private final String KEY_ID = "KeyId";
  private final MessageRepository messageRepo;
  private final AmazonS3 s3Client;

  @Autowired
  public MessageServiceImpl(MessageRepository messageRepo, AmazonS3 s3Client) {
    this.messageRepo = messageRepo;
    this.s3Client = s3Client;
  }

  @Override
  public boolean uploadVideo(MultipartFile file) {
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
}

package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.demo.data.MessageObj;
import com.example.demo.repositories.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepo;
  private final AmazonS3 s3Client;

  @Autowired
  public MessageServiceImpl(MessageRepository messageRepo, AmazonS3 s3Client) {
    this.messageRepo = messageRepo;
    this.s3Client = s3Client;
  }

  @Override
  public boolean createMessage(MessageObj message) {
    return messageRepo.createMessage(message);
  }

  @Override
  public MessageObj getMessage(String messageId) {
    return messageRepo.getMessage(messageId);
  }

  @Override
  public List<MessageObj> getLastHourMessages() {
    return messageRepo.getLastHourMessages();
  }

  @Override
  public boolean uploadVideo(MultipartFile file) {
    List<Bucket> buckets = s3Client.listBuckets();
    System.out.println("Your Amazon S3 buckets are:");
    for (Bucket b : buckets) {
      System.out.println("* " + b.getName());
    }
    try {
      System.out.println("reached here1");
      InputStream is = file.getInputStream();
      System.out.println("reached here2");
      ObjectMetadata meta = new ObjectMetadata();
      System.out.println("reached here3");
      byte[] bytes = IOUtils.toByteArray(is);
      System.out.println("reached here4");

      System.out.println("bytes" + bytes.toString());

      meta.setContentLength(bytes.length);
      System.out.println("reached here5");
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      System.out.println("reached here6");

      String key = "image" + String.valueOf(System.currentTimeMillis());
      String bucket = "my-bucket-images";
      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucket, key, byteArrayInputStream, meta);
      System.out.println("reached here7");
      putObjectRequest.setMetadata(meta);
      s3Client.putObject(putObjectRequest);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    return true;

  }

}

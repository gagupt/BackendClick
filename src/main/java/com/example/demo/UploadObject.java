package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadObject {

	public static void main(String[] args) throws IOException {
		String clientRegion = "us-east-1";
		String bucketName = "my-bucket";
		String stringObjKeyName = "2018/1/1/2646877.mp4";
		String fileObjKeyName = "my-bucket";
		String fileName = "/Users/gaurav/Downloads/2646877.mp4";

		try {
			EndpointConfiguration config = new EndpointConfiguration("http://localhost:4572", clientRegion);
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true)
					.withCredentials(new ProfileCredentialsProvider()).withEndpointConfiguration(config).build();

			// s3Client.createBucket("new");

			List<Bucket> buckets = s3Client.listBuckets();
			System.out.println("Your Amazon S3 buckets are:");
			for (Bucket b : buckets) {
				System.out.println("* " + b.getName());// s3Client.getBucketLocation(new
														// GetBucketLocationRequest(b.getName())));
			}
			// System.out.println( "Content"+
			// s3Client.getObject(bucketName,"abc").getObjectContent());
			System.out.println("END");

			// if (!s3Client.doesBucketExistV2(bucketName)) {
			// Because the CreateBucketRequest object doesn't specify a region, the
			// bucket is created in the region specified in the client.
			// s3Client.createBucket(new CreateBucketRequest(bucketName));

			// Verify that the bucket was created by retrieving it and checking its
			// location.
			// String bucketLocation = s3Client.getBucketLocation(new
			// GetBucketLocationRequest(bucketName));
			// System.out.println("Bucket location: " + bucketLocation);

			// Upload a text string as a new object.
//         s3Client.putObject(bucketName, stringObjKeyName, "Uploaded String Object");
//         
//         // Upload a file as a new object with ContentType and title specified.
			PutObjectRequest request = new PutObjectRequest(bucketName, stringObjKeyName, new File(fileName));
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType("plain/text");
			metadata.addUserMetadata("x-amz-meta-title", "someTitle");
			request.setMetadata(metadata);
			s3Client.putObject(request);
//         
//         
//         System.out.println("Listing objects");
//         ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest()
//                 .withBucketName(bucketName));
//                 
//         for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//             System.out.println(" - " + objectSummary.getKey() + "  " +
//                     "(size = " + objectSummary.getSize() + ")");
//         }
//         System.out.println();
//         
//         
			System.out.println("Success");
			// }
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}
	}
}
package br.com.localstack.s3;

import java.net.URI;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

class S3Service {

	private S3Client s3Client;
	private Region region;

	public S3Service(final URI endpoint) {
		this.region = Region.US_EAST_1;
		this.s3Client = S3Client.builder()
				.region(region)
				.endpointOverride(endpoint)
				.build();
	}

	public void createBucket(final String bucketName) {
		CreateBucketConfiguration bucketConfiguration = CreateBucketConfiguration.builder()
				.locationConstraint(this.region.id())
				.build();
		CreateBucketRequest request = CreateBucketRequest.builder()
				.bucket(bucketName)
				.createBucketConfiguration(bucketConfiguration)
				.build();
		this.s3Client.createBucket(request);
	}

	public ListBucketsResponse listBuckets() {
		return s3Client.listBuckets();
	}
}

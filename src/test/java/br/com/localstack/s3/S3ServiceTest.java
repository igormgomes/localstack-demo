package br.com.localstack.s3;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cloud.localstack.docker.LocalstackDocker;
import cloud.localstack.docker.LocalstackDockerTestRunner;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@RunWith(LocalstackDockerTestRunner.class)
@LocalstackDockerProperties(randomizePorts = true, services = {"s3"}, imageTag = "0.9.3")
public class S3ServiceTest {

	private static final String BUCKET_NAME = "test-bucket";

	private final S3Service s3Service = new S3Service(URI.create(LocalstackDocker.INSTANCE.getEndpointS3()));

	@Before
	public void createBucket() {
		s3Service.createBucket(BUCKET_NAME);
	}

	@Test
	public void shouldtestCreateBucket(){
		ListBucketsResponse bucketsResponse = s3Service.listBuckets();

		assertThat(bucketsResponse.buckets().size(), is(equalTo(1)));
		assertThat(bucketsResponse.buckets().get(0).name(), is(equalTo(BUCKET_NAME)));
	}
}

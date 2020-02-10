package br.com.localstack.sqs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import cloud.localstack.docker.LocalstackDocker;
import cloud.localstack.docker.LocalstackDockerTestRunner;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.Message;

@RunWith(LocalstackDockerTestRunner.class)
@LocalstackDockerProperties(randomizePorts = true, services = {"sqs"}, imageTag = "0.9.3")
public class SqsServiceTest {

	private final SqsService sqsService = new SqsService(URI.create(LocalstackDocker.INSTANCE.getEndpointSQS()));

	@Test
	public void shouldTestCreateQueue() {
		String queueName = "queue-test";
		CreateQueueResponse queue = this.sqsService.createQueue(queueName);
		String endpointSqs = LocalstackDocker.INSTANCE.getEndpointSQS();
		
		assertThat(queue.queueUrl(), is(equalTo(String.format("%s%s%s", endpointSqs, "/queue/", queueName))));
	}

	@Test
	public void shouldTestCreateAndReceiveQueue() {
		CreateQueueResponse queueResponse = sqsService.createQueue("queue-test-old");
		String testMessage = "message -test";

		sqsService.sendMessage(queueResponse.queueUrl(), testMessage);

		List<Message> messages = sqsService.receiveMessage(queueResponse.queueUrl());

		assertThat(messages.size(), is(equalTo(1)));
		assertThat(messages.get(0).body(), is(equalTo(testMessage)));
	}
}
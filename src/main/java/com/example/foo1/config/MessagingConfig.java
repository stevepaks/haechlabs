package com.example.foo1.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import static java.util.Collections.singletonList;

@Configuration
public class MessagingConfig {

    @Bean
    @Primary
    public AwsClientBuilder.EndpointConfiguration endpointConfiguration(@Value("${cloud.aws.region.static}") final String region) {
        return new AwsClientBuilder.EndpointConfiguration("http://localhost:4576", region);
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(@Value("${cloud.aws.credentials.access-key}") final String awsAccesskey,
                                         @Value("${cloud.aws.credentials.secret-key}") final String awsSecretKey,
                                         @Value("${cloud.aws.sqs.pending-transaction-queue.name}") final String pendingTransactionEventQueueName,
                                         @Value("${cloud.aws.sqs.update-account-balance-queue.name}") final String updateAccountBalanceQueueName,
                                         @Value("${cloud.aws.sqs.append-account-transaction-queue.name}") final String appendAccountTransactionQueueName,
                                         final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
        final AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(awsAccesskey, awsSecretKey)
                ))
                .build();
        createQueues(amazonSQSAsync, pendingTransactionEventQueueName);
        createQueues(amazonSQSAsync, updateAccountBalanceQueueName);
        createQueues(amazonSQSAsync, appendAccountTransactionQueueName);
        return amazonSQSAsync;
    }

    private void createQueues(final AmazonSQSAsync amazonSQSAsync,
                              final String queueName) {
        amazonSQSAsync.createQueue(queueName);
        var queueUrl = amazonSQSAsync.getQueueUrl(queueName).getQueueUrl();
        amazonSQSAsync.purgeQueueAsync(new PurgeQueueRequest(queueUrl));
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(MessageConverter messageConverter) {

        var factory = new QueueMessageHandlerFactory();
        factory.setArgumentResolvers(singletonList(new PayloadMethodArgumentResolver(messageConverter)));
        return factory;
    }

    @Bean
    protected MessageConverter messageConverter(ObjectMapper objectMapper) {

        var converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setSerializedPayloadClass(String.class);
        converter.setStrictContentTypeMatch(false);
        return converter;
    }
}

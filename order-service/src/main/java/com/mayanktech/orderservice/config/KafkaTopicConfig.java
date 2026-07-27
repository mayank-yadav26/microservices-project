package com.mayanktech.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	@Bean
	NewTopic notificationTopic() {
		return TopicBuilder.name("notificationTopic")
				.partitions(1)
				.replicas(1)
				.build();
	}
}

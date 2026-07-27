package com.mayanktech.notificationservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.mayanktech.notificationservice.event.OrderPlacedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationConsumer {

	@KafkaListener(topics = "notificationTopic")
	public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
		// send out an email notification.
		log.info("Received notification for order : {}", orderPlacedEvent.getOrderNumber());
	}
}

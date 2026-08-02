package com.mayanktech.notificationservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.mayanktech.common.event.OrderPlacedEvent;
import com.mayanktech.notificationservice.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationService notificationService;

	@KafkaListener(topics = "notificationTopic")
	public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
		// send out an email notification.
		log.info("Received notification for order : {}", orderPlacedEvent.getOrderNumber());
		notificationService.broadcast(orderPlacedEvent.getOrderNumber());
	}
}

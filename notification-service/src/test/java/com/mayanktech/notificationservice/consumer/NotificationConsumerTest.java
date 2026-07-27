package com.mayanktech.notificationservice.consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mayanktech.common.event.OrderPlacedEvent;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

	@InjectMocks
	private NotificationConsumer notificationConsumer;

	@Test
	void shouldHandleNotificationWithoutException() {
		OrderPlacedEvent event = new OrderPlacedEvent("order-123");

		assertDoesNotThrow(() -> notificationConsumer.handleNotification(event));
	}
}

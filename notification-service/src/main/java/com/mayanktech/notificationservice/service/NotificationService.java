package com.mayanktech.notificationservice.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	public SseEmitter subscribe() {
		SseEmitter emitter = new SseEmitter(0L);
		emitters.add(emitter);
		emitter.onCompletion(() -> emitters.remove(emitter));
		emitter.onTimeout(() -> emitters.remove(emitter));
		emitter.onError(e -> emitters.remove(emitter));
		return emitter;
	}

	public void broadcast(String orderNumber) {
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().name("order-notification").data(orderNumber));
			} catch (IOException | IllegalStateException e) {
				emitters.remove(emitter);
				log.warn("Removed stale SSE emitter: {}", e.getMessage());
			}
		}
	}
}

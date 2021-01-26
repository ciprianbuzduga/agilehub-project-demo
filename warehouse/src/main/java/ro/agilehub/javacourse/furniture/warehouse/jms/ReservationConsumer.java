package ro.agilehub.javacourse.furniture.warehouse.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import ro.agilehub.javacourse.furniture.warehouse.api.model.ReservationDTO;
import ro.agilehub.javacourse.furniture.warehouse.service.ReservationService;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {

	private final ReservationService service;

	@JmsListener(destination = "${activemq.destination}",
			containerFactory = "jmsListenerContainerFactory")
	public void receiveNotification(Message<String> message) {
		String payload = message.getPayload();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ReservationDTO reservation = objectMapper.readValue(payload, ReservationDTO.class);
			service.addReservation(reservation);
			System.out.println("Got a new reservation...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package ro.agilehub.javacourse.furniture.warehouse.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import ro.agilehub.javacourse.furniture.warehouse.api.model.ReservationDTO;
import ro.agilehub.javacourse.furniture.warehouse.api.specification.ReservationsApi;
import ro.agilehub.javacourse.furniture.warehouse.service.ReservationService;

@RestController
@RequiredArgsConstructor
public class ReservationController implements ReservationsApi {

	private final ReservationService service;

	@CrossOrigin(origins = "http://localhost:8090")
	@Override
	public ResponseEntity<Void> createResevation(@Valid ReservationDTO reservationDTO) {
		String resId = service.addReservation(reservationDTO);
		if(resId != null) {
			UriComponents uriComponents = UriComponentsBuilder.newInstance()
					.scheme("http").host("localhost").port(8080)
					.path("/reservations/{id}").buildAndExpand(resId);
			return ResponseEntity.created(uriComponents.toUri()).build();
		} else
			throw new ServerErrorException("Cannot add the reservation because of "
					+ "unknown reasone", (Throwable)null);
	}

	@Override
	public ResponseEntity<ReservationDTO> getReservation(String id) {
		ReservationDTO resDto = service.getReservation(id);
		return ResponseEntity.ok(resDto);
	}

	@Override
	public ResponseEntity<Void> deleteReservation(String id) {
		boolean cancelled = service.cancelReservationById(id);
		if(cancelled) {
			return ResponseEntity.noContent().build();
		} else 
			throw new ServerErrorException("Cannot cancel the reservation " + id
					+ " because of unknown reasone", (Throwable)null);
	}

	@Override
	public ResponseEntity<List<ReservationDTO>> getReservations() {
		List<ReservationDTO> list = service.getAllReservations();
		return ResponseEntity.ok(list);
	}
}

package ro.agilehub.javacourse.furniture.warehouse.service;

import java.util.List;

import ro.agilehub.javacourse.furniture.warehouse.api.model.ReservationDTO;

public interface ReservationService {

	String addReservation(ReservationDTO reservationDTO);

	ReservationDTO getReservation(String id);

	boolean cancelReservationById(String id);

	List<ReservationDTO> getAllReservations();

}

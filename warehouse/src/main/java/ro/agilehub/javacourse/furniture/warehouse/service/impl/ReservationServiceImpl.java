package ro.agilehub.javacourse.furniture.warehouse.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ro.agilehub.javacourse.furniture.warehouse.api.model.ReservationDTO;
import ro.agilehub.javacourse.furniture.warehouse.documents.ReservationDoc;
import ro.agilehub.javacourse.furniture.warehouse.mapper.ReservationMapper;
import ro.agilehub.javacourse.furniture.warehouse.repository.ReservationRepository;
import ro.agilehub.javacourse.furniture.warehouse.service.ReservationService;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository repository;
	private final ReservationMapper mapper;

	@Override
	public String addReservation(ReservationDTO reservationDTO) {
		ReservationDoc reservation = mapper.mapToReservationDoc(reservationDTO);
		if(reservation.getReservationDate() == null)
			reservation.setReservationDate(LocalDate.now());
		try {
			repository.save(reservation);
			return reservation.get_id();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ReservationDTO getReservation(String id) {
		ReservationDoc doc = getReservationDoc(id);
		return mapper.mapToReservationDTO(doc);
	}

	private ReservationDoc getReservationDoc(String id) {
		ReservationDoc doc = repository.findById(id).orElseThrow(
				() -> new NoSuchElementException("No reservation found with id " + id));
		return doc;
	}

	@Override
	public boolean cancelReservationById(String id) {
		ReservationDoc doc = getReservationDoc(id);
		// TODO set CANCEL status
		try {
			repository.delete(doc);
			return true;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<ReservationDTO> getAllReservations() {
		List<ReservationDoc> listDocs = repository.findAll();
		if (listDocs == null || listDocs.isEmpty())
			return Collections.emptyList();
		return listDocs.stream().map(mapper::mapToReservationDTO)
				.collect(Collectors.toList());
	}

}

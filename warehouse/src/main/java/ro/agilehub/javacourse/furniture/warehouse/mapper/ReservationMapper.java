package ro.agilehub.javacourse.furniture.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ro.agilehub.javacourse.furniture.warehouse.api.model.ReservationDTO;
import ro.agilehub.javacourse.furniture.warehouse.documents.ReservationDoc;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

	@Mapping(target = "name", source = "productName")
	ReservationDoc mapToReservationDoc(ReservationDTO dto);

	@Mapping(target = "productId", source = "_id")
	@Mapping(target = "productName", source = "name")
	ReservationDTO mapToReservationDTO(ReservationDoc doc);
}

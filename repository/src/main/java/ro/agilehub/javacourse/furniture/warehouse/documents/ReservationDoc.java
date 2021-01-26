package ro.agilehub.javacourse.furniture.warehouse.documents;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document(collection = "reservation")
@Data
@EqualsAndHashCode(of = "_id")
public class ReservationDoc {

	@Id
	private String _id;
	
	private Client client;
	
	private Consultant consultant;
	
	private String name;
	
	private BigDecimal price;
	
	private Integer quantity;
	
	private List<Component> components;
	
	private LocalDate reservationDate;
}

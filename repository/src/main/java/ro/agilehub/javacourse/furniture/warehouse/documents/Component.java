package ro.agilehub.javacourse.furniture.warehouse.documents;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "name")
public class Component {

	private String name;
	
	private Integer quantity;
}

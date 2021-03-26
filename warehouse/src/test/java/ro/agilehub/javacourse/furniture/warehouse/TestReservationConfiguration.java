package ro.agilehub.javacourse.furniture.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import ro.agilehub.javacourse.furniture.warehouse.config.ActiveMQConfig;
import ro.agilehub.javacourse.furniture.warehouse.jms.ReservationConsumer;

@ComponentScan(basePackages = "ro.agilehub.javacourse.furniture.warehouse",
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
	value = {ActiveMQConfig.class, ReservationConsumer.class}))
@SpringBootApplication
public class TestReservationConfiguration {

	public static void main(final String[] args) {
		SpringApplication.run(TestReservationConfiguration.class, args);
	}
}

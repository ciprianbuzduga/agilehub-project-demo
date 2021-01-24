package ro.agilehub.javacourse.furniture.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "ro.agilehub.javacourse.furniture.warehouse")
@SpringBootApplication
public class FurnitureWarehouseApplication {

    public static void main(final String[] args) {
        SpringApplication.run(FurnitureWarehouseApplication.class, args);
    }
}

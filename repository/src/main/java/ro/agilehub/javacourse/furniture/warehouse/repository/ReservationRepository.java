package ro.agilehub.javacourse.furniture.warehouse.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ro.agilehub.javacourse.furniture.warehouse.documents.ReservationDoc;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationDoc, String>{

}

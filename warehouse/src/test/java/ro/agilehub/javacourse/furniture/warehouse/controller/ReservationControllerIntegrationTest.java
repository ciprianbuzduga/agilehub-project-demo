package ro.agilehub.javacourse.furniture.warehouse.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import ro.agilehub.javacourse.furniture.warehouse.api.model.ClientDTO;
import ro.agilehub.javacourse.furniture.warehouse.api.model.ComponentDTO;
import ro.agilehub.javacourse.furniture.warehouse.api.model.ConsultantDTO;
import ro.agilehub.javacourse.furniture.warehouse.api.model.ReservationDTO;
import ro.agilehub.javacourse.furniture.warehouse.documents.ReservationDoc;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationControllerIntegrationTest {

	private static final String PATH = "/reservations";

	private MockMvc mvc;

	@Autowired
	protected WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@After
	public void clean() {
		mongoTemplate.dropCollection(ReservationDoc.COLLECTION_NAME);
	}

	@Test
	public void test_flux_reservation() throws Exception {
		ReservationDTO resReq = createReservation();

		// createReservation
		MvcResult mvcResult = mvc
				.perform(post(PATH).contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(resReq)))
				.andExpect(status().isCreated()).andReturn();

		// getReservation
		String headerLocationId = mvcResult.getResponse().getHeader(LOCATION);
		assertNotNull(headerLocationId);
		assertNotEquals("", headerLocationId);

		String path = getPath(headerLocationId);
		mvcResult = mvc.perform(get(path)).andExpect(status().isOk()).andReturn();

		ReservationDTO resResp = objectMapper.readValue(mvcResult.getResponse()
				.getContentAsString(),
				ReservationDTO.class);
		assertGetReservation(resReq, path, resResp);

		//getAllReservations
		mvcResult = mvc.perform(get(PATH)).andExpect(status().isOk()).andReturn();
		CollectionType javaType = objectMapper.getTypeFactory()
				.constructCollectionType(List.class, ReservationDTO.class);
		List<ReservationDTO> savedReservations = objectMapper.readValue
				(mvcResult.getResponse().getContentAsString(), javaType);

		assertEquals(1, savedReservations.size());
		assertGetReservation(resReq, path, savedReservations.get(0));
		
		//deleteReservation
		mvcResult = mvc.perform(delete(path))
				.andExpect(status().isNoContent())
				.andReturn();

		//getReservation again should return Not Found
		mvcResult = mvc.perform(get(path)).andExpect(status().isNotFound())
				.andReturn();
	}

	private void assertGetReservation(ReservationDTO resReq, String path, ReservationDTO resResp) {
		assertEquals(path.replace(PATH + "/", ""), resResp.getProductId());
		assertEquals(resReq.getClient().getFirstname(), resResp.getClient().getFirstname());
		assertEquals(resReq.getClient().getLastname(), resResp.getClient().getLastname());
		assertEquals(resReq.getClient().getPhone(), resResp.getClient().getPhone());

		ComponentDTO comp1 = resReq.getComponents().get(0);
		ComponentDTO comp2 = resReq.getComponents().get(1);

		ComponentDTO comp3 = resResp.getComponents().get(0);
		ComponentDTO comp4 = resResp.getComponents().get(1);
		assertEquals(comp1.getName(), comp3.getName());
		assertEquals(comp2.getName(), comp4.getName());
		assertEquals(comp1.getQuantity(), comp3.getQuantity());
		assertEquals(comp2.getQuantity(), comp4.getQuantity());

		ConsultantDTO consReq = resReq.getConsultant();
		ConsultantDTO consResp = resResp.getConsultant();
		assertEquals(consReq.getFirstname(), consResp.getFirstname());
		assertEquals(consReq.getLastname(), consResp.getLastname());
		assertEquals(consReq.getPhone(), consResp.getPhone());
		assertEquals(consReq.getEmail(), consResp.getEmail());

		assertEquals(resReq.getPrice(), resResp.getPrice());
		assertEquals(resReq.getProductName(), resResp.getProductName());
		assertEquals(resReq.getQuantity(), resResp.getQuantity());
		assertEquals(resReq.getReservationDate(), resResp.getReservationDate());
	}

	private ReservationDTO createReservation() {
		ReservationDTO reservation = new ReservationDTO();
		ClientDTO client = new ClientDTO().firstname("john").lastname("doe").phone("07467889890");
		reservation.setClient(client);

		ComponentDTO component1 = new ComponentDTO().name("table").quantity(1);
		ComponentDTO component2 = new ComponentDTO().name("chair").quantity(2);
		reservation.setComponents(List.of(component1, component2));

		ConsultantDTO consultant = new ConsultantDTO().firstname("mike").lastname("clue").phone("07467889890")
				.email("mike@yahoo.com");
		reservation.setConsultant(consultant);
		reservation.setPrice(new BigDecimal(1000));
		reservation.setProductName("Room table");
		reservation.setQuantity(1);
		reservation.setReservationDate(LocalDate.now());
		return reservation;
	}

	private static String getPath(String spec) throws MalformedURLException {
		URL url = new URL(spec);
		String path = url.getPath();
		return path;
	}
}

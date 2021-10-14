package com.emanuel.birras.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emanuel.birras.model.Meetup;
import com.emanuel.birras.model.Temperature;
import com.emanuel.birras.model.User;
import com.emanuel.birras.service.MeetupServiceImpl;
import com.emanuel.birras.service.MeetupUsersServiceImpl;
import com.emanuel.birras.service.TemperatureServiceImpl;
import com.emanuel.birras.service.UserServiceImpl;
import com.emanuel.birras.utils.ApiError;
import com.emanuel.birras.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("api/meetups/")
public class MeetupREST {
	@Autowired
	private MeetupServiceImpl meetupServiceImpl;
	
	@Autowired
	private TemperatureServiceImpl temperatureServiceImpl;
	
	@Autowired
	private MeetupUsersServiceImpl meetupUsersServiceImpl;
	
	@Autowired
	private UserServiceImpl UserServiceImpl;
	
	
    @Value("${configuration.latitud}")
    private String latitud;
	
    @Value("${configuration.longitud}")
    private String longitud;
    
    @Autowired
    private JWTUtil jwtUtil;
    
	@GetMapping()
	private ResponseEntity<List<Meetup>> getAllMeetups() {
		return ResponseEntity.ok(meetupServiceImpl.findAll());
	}
	
	@GetMapping(value = "getOne/{idMeetup}")
	private Object getOneMeetUp(@PathVariable ("idMeetup") Long idMeetup) {
		
		Optional<Meetup> meetup = meetupServiceImpl.findById(idMeetup);
		
		LinkedHashMap<String, Object> resp = new LinkedHashMap<String, Object>();
		resp.put("id", meetup.get().getId());
		resp.put("name", meetup.get().getName());
		resp.put("address", meetup.get().getAddress());
		String fechaAux = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(meetup.get().getDateFrom());
		resp.put("date", fechaAux);
		resp.put("hours", meetup.get().getHours());
		
		return ResponseEntity.ok(resp);
	}
	
	LinkedHashMap<String, Object> quantityBirras(List<Temperature> t, Integer sizeMeetup) {
		Double quantityBirras = 0.0;
		quantityBirras = (t.get(0).getTemperature() < 20) ? 0.75 * sizeMeetup.doubleValue() : quantityBirras;
		quantityBirras = (t.get(0).getTemperature() >= 20) && (t.get(0).getTemperature() <= 24) ? 1 * sizeMeetup.doubleValue() : quantityBirras;
		quantityBirras = (t.get(0).getTemperature() > 24) ? 2 * sizeMeetup.doubleValue() : quantityBirras;
		quantityBirras = Math.ceil(quantityBirras);
		Double quantityBoxBirras = Math.ceil(quantityBirras / 6);
		
		LinkedHashMap<String, Object> resp = new LinkedHashMap<String, Object>();
		resp.put("temperature", t.get(0).getTemperature());
		resp.put("quantityBoxBirras", quantityBoxBirras.intValue());
		
		return resp;
	}
	
	private Object callTemperatureUrl() throws IOException, InterruptedException {

		HttpResponse<String> response;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/daily?lat=" + latitud + "&lon=" + longitud))
				.header("x-rapidapi-host", "weatherbit-v1-mashape.p.rapidapi.com")
				.header("x-rapidapi-key", "bbe67d03c0msh28a2be64846e340p1cc9efjsn9935f02ec359")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		
		response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		Object responseTemp = new ObjectMapper().readValue(response.body(), Object.class);
		return responseTemp;

	}
	
	@GetMapping(value = "temperature/{idMeetup}")
	private ResponseEntity<Object> getTemperatureMeetup(@PathVariable ("idMeetup") Long idMeetup) {
		
		//TODO: this must to be refactorized. Must to correct the warnings too.
		
		try {	
			//TODO: this is making a 500 error, must capture better this error
			Optional<Meetup> meetup = meetupServiceImpl.findById(idMeetup);
			if (meetup == null || meetup.get().getId() == null) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
			
			List<Temperature> t = temperatureServiceImpl.findByDate(meetup.get().getDateFrom());
			if (t.size() > 0) {
				if (!temperatureServiceImpl.saveTemperature(Timestamp.valueOf(meetup.get().getDateFrom().toLocalDateTime().toLocalDate().atStartOfDay()), Timestamp.valueOf(LocalDateTime.now()))) {
					//return ResponseEntity.ok(quantityBirras(t, meetupUsersServiceImpl.findAllByMeetup(idMeetup).size()));
					return ResponseEntity.ok(quantityBirras(t, meetupUsersServiceImpl.findByMeetup(meetup.get()).size()));
				}
			}
			
			Object responseTemp = callTemperatureUrl();
			
			LinkedHashMap<String, ArrayList<Object>> map = (LinkedHashMap<String, ArrayList<Object>>)responseTemp;
			ArrayList<Object> arrayObject = map.get("data");
			for (Object data: arrayObject) {
				Object date = ((LinkedHashMap<String, Object>) data).get("datetime");
				Object temp = ((LinkedHashMap<String, Object>) data).get("max_temp");
				Temperature temperature = new Temperature();
				
				String dateToParse = ((String) date) + " 00:00";
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime dateTime = LocalDateTime.parse(dateToParse, formatter);
				
				temperature.setDate(Timestamp.valueOf(dateTime));
				if (temp.getClass() == Integer.class) {
					temp = ((Integer) temp).doubleValue();
				}
				
				temperature.setTemperature((Double)temp);
				temperature.setDateModified(Timestamp.valueOf(LocalDateTime.now()));
				temperatureServiceImpl.save(temperature);
			}
			t = temperatureServiceImpl.findByDate(meetup.get().getDateFrom());
			if (t.size() > 0) {
				return ResponseEntity.ok(quantityBirras(t, meetupUsersServiceImpl.findByMeetup(meetup.get()).size()));
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		} catch (IOException | InterruptedException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		
	}
	
	@PostMapping()
	private ResponseEntity<Object> postMeetup(@RequestBody Meetup meetup, @RequestHeader(value="Authorization") String token) {
		try {
			if (jwtUtil.getKey(token)==null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		String id = jwtUtil.getKey(token);
		Optional<User> userAux = UserServiceImpl.findById(Long.parseLong(id));
		
		if (userAux.get().getIsAdmin() == null || !userAux.get().getIsAdmin()) { //puede venir nulo, por eso hago el == true
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		try {
			if (meetup.getId() != null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "you musn't pass an id", "you musn't pass an id"));
			}
			meetup.setDateTo(Timestamp.valueOf((meetup.getDateFrom().toLocalDateTime().plusHours(meetup.getHours()))));
			Meetup meetupSaved = meetupServiceImpl.save(meetup);
			return ResponseEntity.created(new URI("/meetups/" + meetupSaved.getId())).body(meetupSaved);
		} catch (DataIntegrityViolationException e) {
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "user exists"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PatchMapping()
	private ResponseEntity<Object> patchMeetup(@RequestBody Meetup meetup, @RequestHeader(value="Authorization") String token) {
		try {
			if (jwtUtil.getKey(token)==null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		String id = jwtUtil.getKey(token);
		Optional<User> userAux = UserServiceImpl.findById(Long.parseLong(id));
		
		if (userAux.get().getIsAdmin() == null || !userAux.get().getIsAdmin()) { //puede venir nulo, por eso hago el == true
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		try {
			if (meetup.getId() == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "you must to pass an id", "you must to pass an id"));
			}
			meetup.setDateTo(Timestamp.valueOf((meetup.getDateFrom().toLocalDateTime().plusHours(meetup.getHours()))));
			Meetup meetupSaved = meetupServiceImpl.save(meetup);
			return ResponseEntity.created(new URI("/meetups/" + meetupSaved.getId())).body(meetupSaved);
		} catch (DataIntegrityViolationException e) {
		    return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "user exists"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@DeleteMapping (value = "delete/{id}")
	private ResponseEntity<Boolean> deleteMeetup (@PathVariable ("id") Long id){
		meetupServiceImpl.deleteById(id);
		return ResponseEntity.ok(!(meetupServiceImpl.findById(id)!=null));
	}
}

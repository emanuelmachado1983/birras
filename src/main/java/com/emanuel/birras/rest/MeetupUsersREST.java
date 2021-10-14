package com.emanuel.birras.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emanuel.birras.model.Meetup;
import com.emanuel.birras.model.MeetupUsers;
import com.emanuel.birras.model.User;

import com.emanuel.birras.service.UserServiceImpl;
import com.emanuel.birras.service.MeetupServiceImpl;
import com.emanuel.birras.service.MeetupUsersServiceImpl;
import com.emanuel.birras.utils.ApiError;
import com.emanuel.birras.utils.JWTUtil;

@RestController
@RequestMapping("api/meetupsUsers/")
public class MeetupUsersREST {
	@Autowired
	private MeetupUsersServiceImpl meetupUsersServiceImpl;
	
	@Autowired
	private MeetupServiceImpl meetupServiceImpl;
	
	@Autowired
	private UserServiceImpl UserServiceImpl;
	
	@Autowired
    private JWTUtil jwtUtil;

	@GetMapping ("{id}")
	private ResponseEntity<Object> getAllMeetupsUsers(@PathVariable("id") Long idMeetup) {
		//TODO: this is making a 500 error, must capture better this error
		Optional<Meetup> meetup = meetupServiceImpl.findById(idMeetup);
		if (meetup == null || meetup.get().getId() == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		List<MeetupUsers> meetupsUsers = meetupUsersServiceImpl.findByMeetup(meetup.get(), Sort.by(Sort.Direction.ASC, "user.name"));
		
		ArrayList<LinkedHashMap<String, Object>> resp = new ArrayList<LinkedHashMap<String, Object>>();
		for (MeetupUsers meetupUser: meetupsUsers) {
			//meetupsWhereIam.add(meetupUser.getMeetup());
			LinkedHashMap<String, Object> aux = new LinkedHashMap<String, Object>();
			aux.put("idMeetupUser", meetupUser.getId());
			aux.put("id", meetupUser.getUser().getId());
			aux.put("name", meetupUser.getUser().getName());
			resp.add(aux);
		}
		
		return ResponseEntity.ok(resp);
	}
	
	
	@GetMapping ("notInvited/{id}")
	private ResponseEntity<Object> getAllMeetupsUsersNotInvited(@PathVariable("id") Long idMeetup) {
		//TODO: this is making a 500 error, must capture better this error
		Optional<Meetup> meetup = meetupServiceImpl.findById(idMeetup);
		if (meetup == null || meetup.get().getId() == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		List<MeetupUsers> meetupsUsers = meetupUsersServiceImpl.findByMeetup(meetup.get());
		
		ArrayList<LinkedHashMap<String, Object>> resp = new ArrayList<LinkedHashMap<String, Object>>();
		//TODO: hacer esto por query.
		
		List<User> users = UserServiceImpl.findAllByOrderByName();
		
		for (User user: users) {
			boolean found = false;
			for (MeetupUsers meetupUser: meetupsUsers) {
				if (meetupUser.getUser().getId().equals(user.getId())) {
					found = true;
				}
			}
			if (!found) {
				LinkedHashMap<String, Object> aux = new LinkedHashMap<String, Object>();
				aux.put("id", user.getId());
				aux.put("name", user.getName());
				resp.add(aux);
			}
		}
		
		return ResponseEntity.ok(resp);
	}
	
	@GetMapping("whereIam/")
	private ResponseEntity<Object> getAllMeetupAdded(@RequestHeader(value="Authorization") String token) {
		try {
			if (jwtUtil.getKey(token)==null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		//Timestamp.valueOf(
		Timestamp now = Timestamp.valueOf((LocalDateTime.now()));
		String id = jwtUtil.getKey(token);
		User userAux = new User();
		userAux.setId(Long.valueOf(id));
		//TODO: solo quiero que me devuelva las meetups, hay que mejorar esto para solo traer las meetups y no andar usando un bucle
		List<MeetupUsers> meetupsUsers = meetupUsersServiceImpl.findByUser(userAux, Sort.by(Sort.Direction.DESC, "meetup.dateFrom"));
		ArrayList<LinkedHashMap<String, Object>> resp = new ArrayList<LinkedHashMap<String, Object>>();
		for (MeetupUsers meetupUser: meetupsUsers) {
			//meetupsWhereIam.add(meetupUser.getMeetup());
			LinkedHashMap<String, Object> aux = new LinkedHashMap<String, Object>();
			aux.put("id", meetupUser.getMeetup().getId());
			aux.put("idMeetupUser", meetupUser.getId());
			aux.put("name", meetupUser.getMeetup().getName());
			aux.put("address", meetupUser.getMeetup().getAddress());
			String fechaAux = "";
			try {
				fechaAux = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(meetupUser.getMeetup().getDateFrom());	
			} catch (Exception e) {
				fechaAux = "";
			}
			aux.put("dateFrom", fechaAux);
			aux.put("hours", meetupUser.getMeetup().getHours());
			aux.put("newOne", now.before(meetupUser.getMeetup().getDateTo()));			
			aux.put("canCheck", now.after(meetupUser.getMeetup().getDateFrom()));
			aux.put("checked", meetupUser.getCheckIn());
			resp.add(aux);
		}
		
		return ResponseEntity.ok(resp);
	}
	
	@GetMapping("whereNotIam/")
	private ResponseEntity<Object> getAllMeetupNotAdded(@RequestHeader(value="Authorization") String token) {
		try {
			if (jwtUtil.getKey(token)==null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		String id = jwtUtil.getKey(token);
		User userAux = new User();
		userAux.setId(Long.valueOf(id));
		Timestamp now = Timestamp.valueOf((LocalDateTime.now()));
		//TODO: hacer esto por query, ahora lo estoy haciendo con un for lo cual no está del todo bien
		List<MeetupUsers> meetupsWherIam = meetupUsersServiceImpl.findByUser(userAux, Sort.by(Sort.Direction.DESC, "meetup.dateFrom"));
		List<Meetup> meetups = meetupServiceImpl.findAll(Sort.by(Sort.Direction.DESC, "dateFrom"));
		ArrayList<Meetup> meetupsWhereIamNot = new ArrayList<Meetup>();
		for (Meetup meetup: meetups) {
			boolean exists =false;
			for (MeetupUsers meetupUser: meetupsWherIam) {
				if (meetupUser.getMeetup().getId().equals(meetup.getId())){
					exists = true;
				}
			}
			if (!exists) {
				meetupsWhereIamNot.add(meetup);
			}
		}
		
		ArrayList<LinkedHashMap<String, Object>> resp = new ArrayList<LinkedHashMap<String, Object>>();
		for (Meetup meetup: meetupsWhereIamNot) {
			//meetupsWhereIam.add(meetupUser.getMeetup());
			LinkedHashMap<String, Object> aux = new LinkedHashMap<String, Object>();
			aux.put("id", meetup.getId());
			aux.put("name", meetup.getName());
			aux.put("address", meetup.getAddress());
			String fechaAux = "";
			try {
				fechaAux = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(meetup.getDateFrom());	
			} catch (Exception e) {
				fechaAux = "";
			}
			aux.put("dateFrom", fechaAux);
			aux.put("hours", meetup.getHours());
			if (now.before(meetup.getDateTo())) {
				resp.add(aux);
			}
		}
		
		return ResponseEntity.ok(resp);
	}
	
	@PostMapping("add/{idMeetup}/{idUser}")
	private ResponseEntity<Object> postMeetupUser(@PathVariable("idMeetup") Long idMeetup, @PathVariable("idUser") Long idUser,
			@RequestHeader(value="Authorization") String token) {
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
			
			MeetupUsers meetupUser = new MeetupUsers();
			Meetup meetup = new Meetup();
			meetup.setId(idMeetup);
			meetupUser.setMeetup(meetup);
			User user = new User();
			user.setId(idUser);
			meetupUser.setUser(user);
			
			//check if user is already in the meetup
			List<MeetupUsers> list = meetupUsersServiceImpl.findByMeetupAndUser(meetup, user);
			if (list.size() > 0) {
			    return ResponseEntity
		                .status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "user already in the meetup", "user already in the meetup"));
			}
			meetupUsersServiceImpl.save(meetupUser);
			return ResponseEntity.ok(true);
		} catch (DataIntegrityViolationException e) {
		    return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "user exists"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PostMapping("addMe/{idMeetup}")
	private ResponseEntity<Object> AddMeMeetupUser(@PathVariable("idMeetup") Long idMeetup, @RequestHeader(value="Authorization") String token) {
		try {
			try {
				if (jwtUtil.getKey(token)==null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }	
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			MeetupUsers meetupUser = new MeetupUsers();
			Meetup meetup = new Meetup();
			meetup.setId(idMeetup);
			meetupUser.setMeetup(meetup);
			String idAux = jwtUtil.getKey(token);
			User userAux = new User();
			userAux.setId(Long.valueOf(idAux));
			meetupUser.setUser(userAux);
			
			//check if user is already in the meetup
			List<MeetupUsers> list = meetupUsersServiceImpl.findByMeetupAndUser(meetup, userAux);
			if (list.size() > 0) {
			    return ResponseEntity
		                .status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "user already in the meetup", "user already in the meetup"));
			}
			meetupUsersServiceImpl.save(meetupUser);
			return ResponseEntity.ok(true);
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
	private ResponseEntity<Object> deleteMeetupUser(@PathVariable ("id") Long id){
		meetupUsersServiceImpl.deleteById(id);
		return ResponseEntity.ok(!(meetupUsersServiceImpl.findById(id)!=null));
	}
	
	@DeleteMapping (value = "deleteMe/{idMeetup}")
	private ResponseEntity<Boolean> deleteMeFromMeetup(@PathVariable ("idMeetup") Long idMeetup, @RequestHeader(value="Authorization") String token){
		try {
			if (jwtUtil.getKey(token)==null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		String idAux = jwtUtil.getKey(token);
		User userAux = new User();
		userAux.setId(Long.valueOf(idAux));
		
		Meetup meetupAux = new Meetup();
		meetupAux.setId(idMeetup);
		
		List<MeetupUsers> meetupUsersAux = meetupUsersServiceImpl.findByMeetupAndUser(meetupAux, userAux);
		
		meetupUsersServiceImpl.deleteById(meetupUsersAux.get(0).getId());
		return ResponseEntity.ok(!(meetupServiceImpl.findById(idMeetup)!=null));
	}
	
	@PutMapping (value = "{id}")
	private ResponseEntity<Boolean> putMeetupUser(@PathVariable ("id") Long id){
		try {
			MeetupUsers meetupUser = (meetupUsersServiceImpl.findById(id)).get();  //TODO: posible null pointer exception
			meetupUser.setCheckIn(true);
			meetupUsersServiceImpl.save(meetupUser);

			return ResponseEntity.ok(meetupUsersServiceImpl.findById(id)!=null);
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PostMapping (value = "changeCheckIn/{id}")
	private ResponseEntity<Boolean> changeCheckInMeetupUser(@PathVariable ("id") Long id){
		try {
			MeetupUsers meetupUser = (meetupUsersServiceImpl.findById(id)).get();  //TODO: posible null pointer exception
			boolean value = meetupUser.getCheckIn() != null && meetupUser.getCheckIn();
			meetupUser.setCheckIn(!value);
			meetupUsersServiceImpl.save(meetupUser);

			return ResponseEntity.ok(meetupUsersServiceImpl.findById(id)!=null);
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}

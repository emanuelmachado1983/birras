package com.emanuel.birras.rest;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.emanuel.birras.model.User;
import com.emanuel.birras.service.UserServiceImpl;
import com.emanuel.birras.utils.ApiError;
import com.emanuel.birras.utils.JWTUtil;


@RestController
@RequestMapping("api/users/")
public class UserREST {
	@Autowired
	private UserServiceImpl userServiceImpl;
	
    @Autowired
    private JWTUtil jwtUtil;

    
	@GetMapping()
	private ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userServiceImpl.findAll());
	}
	
	
	@PostMapping()
	private ResponseEntity<Object> postUser(@RequestBody User user) {
		try {
			if (user.getId() != null) {
				 return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR,"only for POST, dont pass the id as parameter", "only for POST, dont pass the id as parameter"));
			}
			
			if (userServiceImpl.findByUserBeer(user.getUserBeer())!= null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "user exists", "user exists"));
			}
			
			if (userServiceImpl.findByEmail(user.getEmail())!= null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "email exists", "email exists"));
			}
			
			User userSaved = userServiceImpl.save(user);
			return ResponseEntity.created(new URI("/users/" + userSaved.getId())).body(userSaved);
		} catch (DataIntegrityViolationException e) {
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "user exists"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	/*
	@PatchMapping()
	private ResponseEntity<Object> patchUser(@RequestBody User user) {
		try {
			if (user.getId() == null) {
				 return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "you must pass the id of user", "you must pass the id of user"));
			}
			if (user.getUserBeer() != null) {
				 return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "you can't change the user", "you can't change the user"));
			}
			
			userServiceImpl.updateUserEmail(user.getId(), user.getEmail(), user.getName());
			return ResponseEntity.ok(true);
		} catch (DataIntegrityViolationException e) {
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "user exists"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
		    	      HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "error"));
		}
	}
	*/
	@DeleteMapping (value = "delete/{id}")
	private ResponseEntity<Boolean> deleteUser (@PathVariable ("id") Long id){
		userServiceImpl.deleteById(id);
		return ResponseEntity.ok(!(userServiceImpl.findById(id)!=null));
	}
	
	@GetMapping (value = "retrieveUserByEmail/{email}")
	private ResponseEntity<Object> getUserByEmail(@PathVariable ("email") String email) {
		User userAux = userServiceImpl.findByEmail(email);

		LinkedHashMap<String, Object> resp = new LinkedHashMap<String, Object>();
		resp.put("user", userAux != null ? userAux.getUserBeer() : "");
		resp.put("foundMail", userAux != null);
		
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
	}
	
	@GetMapping (value = "getMyUser/")
	private ResponseEntity<Object> getUser(@RequestHeader(value="Authorization") String token) {
		try {
			if (jwtUtil.getKey(token)==null) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		String id = jwtUtil.getKey(token);
		Optional<User> userAux = userServiceImpl.findById(Long.parseLong(id));
		
		
		LinkedHashMap<String, Object> resp = new LinkedHashMap<String, Object>();
		resp.put("admin", userAux.get().getIsAdmin());
		resp.put("user", userAux.get().getUserBeer());
		resp.put("mail", userAux.get().getEmail());
		
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
	}
}

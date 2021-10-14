package com.emanuel.birras.rest;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emanuel.birras.model.User;
import com.emanuel.birras.service.UserServiceImpl;
import com.emanuel.birras.utils.ApiError;
import com.emanuel.birras.utils.ApiToken;
import com.emanuel.birras.utils.JWTUtil;

@RestController
@RequestMapping("api/login/")
public class AuthREST {
	@Autowired
	private UserServiceImpl userServiceImpl;
	
    @Autowired
    private JWTUtil jwtUtil;
	
	@PostMapping()
	private ResponseEntity<Object> postMeetup(@RequestBody User user) {
		try {
			User userDb = userServiceImpl.findByUserBeer(user.getUserBeer());
			if (userDb!=null && user.getPassword().equals(userDb.getPassword())) {
				String tokenJwt = jwtUtil.create(String.valueOf(userDb.getId()), userDb.getUserBeer());
				ApiToken apiToken = new ApiToken(tokenJwt);
				return ResponseEntity
		                .status(HttpStatus.OK)
		                .body(apiToken);
			} else {
			    return ResponseEntity
		                .status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body(new ApiError(
		  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "ERROR LOGIN", "ERROR LOGIN"));
			}
		} catch (Exception e) {
			return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "ERROR LOGIN", "ERROR LOGIN"));
		}
	}
	
	
	@PatchMapping()
	private ResponseEntity<Object> modificarPassword(@RequestBody User user) {
		try {
			if (user.getUserBeer() == null) {
				 return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiError(
	  		    	      HttpStatus.INTERNAL_SERVER_ERROR, "you must pass the id of user", "you must pass the id of user"));
			}
			userServiceImpl.updateUserPassword(user.getUserBeer(), user.getPassword());
			
			return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(true);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(
		    	      HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "error"));
		}
	}
}

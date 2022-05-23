package com.nicico.ibs.controller;

import com.nicico.copper.common.Loggable;
import com.nicico.ibs.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users/authenticate")
public class UserRestController {

    private final ModelMapper modelMapper;
	// ------------------------------

	@Loggable
	@PostMapping
	public ResponseEntity<User> create(@RequestBody String request, Principal principal) {
		UsernamePasswordAuthenticationToken  a= ((UsernamePasswordAuthenticationToken) principal);
		User u= modelMapper.map( a.getPrincipal(), User.class);
		u.setPassword("0");
		u.setPassword2("0");
		return new ResponseEntity<>(u, HttpStatus.OK);
	}

}

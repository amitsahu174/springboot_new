package com.jpatry.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jpatry.model.JwtRequest;
import com.jpatry.model.JwtResponse;
import com.jpatry.service.UserService;
import com.jpatry.utility.JWTUtility;

@RestController
public class Mycontroller {
	   @Autowired
	    private JWTUtility jwtUtility;

	    @Autowired
	    private AuthenticationManager authenticationManager;

	    @Autowired
	    private UserService userService;

	    @GetMapping("/")
	    public String home() {
	        return "Welcome to Daily Code Buffer!!";
	    }

	    @PostMapping("/authenticate")
	    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception
	    {
	        try {
	            authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(
	                            jwtRequest.getUsername(),
	                            jwtRequest.getPassword()
	                    )
	            );
	        } catch (BadCredentialsException e) {
	            throw new Exception("INVALID_CREDENTIALS", e);
	        }

	        final UserDetails userDetails
	                = userService.loadUserByUsername(jwtRequest.getUsername());

	        final String token =
	                jwtUtility.generateToken(userDetails);

	        System.out.println(token);
	        return ResponseEntity.ok(new JwtResponse(token)) ;
		       }
}

package com.vtube.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.dto.SignUpDTO;
import com.vtube.dto.UserDTO;
import com.vtube.security.PasswordHandler;
import com.vtube.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	@ResponseBody
	public UserDTO signUp(@RequestBody SignUpDTO signUpData, HttpServletRequest request){
		//encrypt user password
		signUpData.setPassword(PasswordHandler.encryptPassword(signUpData.getPassword()));
		
		HttpSession session = request.getSession();
		UserDTO user = null;
		
		user = this.userService.createUser(signUpData);
		
		session.setAttribute("userId", user.getId());
		
		return user;
	}
	
	@GetMapping("/")
	public void writeSomething(HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();
			writer.println("Test");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

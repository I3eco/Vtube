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

import com.vtube.UserValidation.InvalidPasswordException;
import com.vtube.UserValidation.UserValidation;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.UserDTO;
import com.vtube.exceptions.EmailExistsException;
import com.vtube.exceptions.InvalidAgeException;
import com.vtube.exceptions.InvalidEmailException;
import com.vtube.exceptions.InvalidNameException;
import com.vtube.exceptions.UserExistsException;
import com.vtube.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	@ResponseBody
	public UserDTO signUp(@RequestBody SignUpDTO signUpData, HttpServletRequest request, HttpServletResponse response) throws EmailExistsException, UserExistsException, InvalidEmailException, InvalidNameException, InvalidPasswordException, InvalidAgeException{
		
		UserValidation userValidation = new UserValidation();
		userValidation.confirm(signUpData);
		
		String email = signUpData.getEmail();
		String nickName = signUpData.getNickName();
		System.out.println(nickName);
		
		userService.validateEmail(email);
		userService.haveSameEmail(email);
		userService.haveSameNickName(nickName);

		//encrypt user password
		signUpData.setPassword(userService.encryptPassword(signUpData.getPassword()));
		
		//create session
		HttpSession session = request.getSession();
		
		//add user to db and return the properp object to be sent as response
		UserDTO user = this.userService.createUser(signUpData);
		
		//add user id to session
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

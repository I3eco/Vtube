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
	public UserDTO signUp(@RequestBody SignUpDTO signUpData, HttpServletRequest request, HttpServletResponse response){
		
		String email = signUpData.getEmail();
		String nickName = signUpData.getNickName();
		System.out.println(nickName);
		
		if(!userService.validateEmail(email)) {
			try {
				response.sendError(400, "Invalid email!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		if(userService.haveSameEmail(email)) {
			try {
				response.sendError(400, "User with this email already exists!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		if(userService.haveSameNickName(nickName)) {
			try {
				response.sendError(400, "User with this nick name already exists!");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
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

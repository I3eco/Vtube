package com.vtube.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.dto.LoginDTO;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.dto.UserDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.exceptions.BadCredentialsException;
import com.vtube.exceptions.EmailExistsException;
import com.vtube.exceptions.InvalidAgeException;
import com.vtube.exceptions.InvalidEmailException;
import com.vtube.exceptions.InvalidNameException;
import com.vtube.exceptions.InvalidPasswordException;
import com.vtube.exceptions.UserExistsException;
import com.vtube.exceptions.UserNotFoundException;
import com.vtube.service.SessionService;
import com.vtube.service.UserService;
import com.vtube.validations.UserValidation;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	SessionService session;
	
	@PostMapping("/signup")
	@ResponseBody
	public UserDTO signUp(@RequestBody SignUpDTO signUpData, HttpServletRequest request, HttpServletResponse response) throws EmailExistsException, UserExistsException, InvalidEmailException, InvalidNameException, InvalidPasswordException, InvalidAgeException{
		
		UserValidation userValidator = userService.getUserValidator();
		userValidator.confirm(signUpData);
		
		String email = signUpData.getEmail();
		String nickName = signUpData.getNickName();
		
		userService.haveSameEmail(email);
		userService.haveSameNickName(nickName);

		//encrypt user password
		signUpData.setPassword(userService.encryptPassword(signUpData.getPassword()));
		
		//add user to db and return the proper object to be sent as response
		UserDTO user = this.userService.createUser(signUpData);
		
//		//create session
//		HttpSession session = request.getSession();
//		
//		//add user id to session
//		session.setAttribute("userId", user.getId());
		
		this.session.createSession(request, user.getId());
		
		return user;
	}
	
	@GetMapping("/")
	public void mainPage(HttpServletResponse response) throws IOException {
		response.sendRedirect("https://vtubeto.postman.co");
	}
	
	@GetMapping("/user/{id}")
	public UserDTO getUserById(@PathVariable long id) throws UserNotFoundException {
		UserDTO user = this.userService.getUserDTOById(id);
		return user;
	}
	
	@PostMapping("/login")
	@ResponseBody
	public SimpleMessageDTO login(@RequestBody LoginDTO user, HttpServletRequest request) throws BadCredentialsException {
		SimpleMessageDTO message = new SimpleMessageDTO();
		if(request.getSession(false) != null) {
			message.setMessage("Someone is already logged in");
		} else {
			Long userId = this.userService.login(user);
			HttpSession session = request.getSession();
			session.setAttribute("userId", userId);
			message.setMessage("You logged in!");
		}
		return message;
	}
	
	@GetMapping("/logout")
	@ResponseBody
	public SimpleMessageDTO logout(HttpServletRequest request){
		SimpleMessageDTO message = new SimpleMessageDTO();
		HttpSession session = request.getSession(false);
		if(session == null) {
			message.setMessage("Noone is logged in!");
		} else {
			session.invalidate();
			message.setMessage("You logged out!");
		}
		
		return message;
	}
	
	@GetMapping("/videos")
	@ResponseBody
	public List<VideoDTO> getUserVideos(@RequestParam(name= "watched", required = false) boolean watched, @RequestParam(name= "liked", required = false) boolean liked, 
			@RequestParam(name= "forLater", required = false) boolean forLater,
			HttpServletRequest request) throws UserNotFoundException{
		Long userId = null;
		try {
			userId = this.session.getUserId(request);
		} catch (UserNotFoundException e) {
			if(liked != true) {
				throw new UserNotFoundException("You must log in to see that!");
			}
		}
		
		if(watched == true) {
			List<VideoDTO> watchedVideos = this.userService.getUserWatchedVideos(userId);
			return watchedVideos;
		}
		
		if(forLater == true) {
			List<VideoDTO> videosForLater = this.userService.getUserVideosForLater(userId);
			return videosForLater;
		}
		
		return null;
	}
	
}

package com.vtube.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.UsersRepository;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.UserDTO;
import com.vtube.model.User;

/**
 * Class to manage database with user related requests.
 * @author I3eco
 *
 */
@Service
public class UserService {
	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public UserDTO createUser(SignUpDTO signUpData) {
		User user = this.modelMapper.map(signUpData, User.class);
		//TODO must make validation if user exists in database

		this.userRepository.save(user);
		
		user = this.userRepository.findUserByEmail(user.getEmail());
		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
		
		return userDTO;
	}
}

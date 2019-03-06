package com.vtube.service;

import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.ChannnelsRepository;
import com.vtube.dal.UsersRepository;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.UserDTO;
import com.vtube.model.Channel;
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
	private ChannnelsRepository channelRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public UserDTO createUser(SignUpDTO signUpData) {
		User user = this.modelMapper.map(signUpData, User.class);
		//TODO must make validation if user exists in database

		this.userRepository.save(user);
		user = this.userRepository.findUserByEmail(user.getEmail()).get();

		UserDTO userDTO = this.convertFromUserToUserDTO(user);
		
		return userDTO;
	}
	
	public UserDTO convertFromUserToUserDTO(User user) {
		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
		
		Channel channel = null;
		try {
			channel = this.channelRepository.findChannelByOwner(user).get();
		} catch (NoSuchElementException e) {
			//do nothing and channel remains null
		}
		
		if (channel != null)
			userDTO.setNumberOfSubscribers((long) channel.getUsersSubscribedToChannel().size());
		
		return userDTO;
		
	}
}

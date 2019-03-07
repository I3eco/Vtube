package com.vtube.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.vtube.dal.ChannnelsRepository;
import com.vtube.dal.UsersRepository;
import com.vtube.dto.LoginDTO;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.UserDTO;
import com.vtube.exceptions.EmailExistsException;
import com.vtube.exceptions.InvalidPasswordException;
import com.vtube.exceptions.UserExistsException;
import com.vtube.exceptions.UserNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.User;
import com.vtube.validations.UserValidation;

/**
 * Class to manage database with user related requests.
 * 
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

	@Autowired
	private UserValidation userValidator;

	public UserDTO createUser(SignUpDTO signUpData) {
		User user = this.modelMapper.map(signUpData, User.class);
		// TODO must make validation if user exists in database

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
			// do nothing and channel remains null
		}

		if (channel != null)
			userDTO.setNumberOfSubscribers((long) channel.getUsersSubscribedToChannel().size());

		return userDTO;

	}

	public UserDTO getUserDTOById(Long id) throws UserNotFoundException {
		User user = null;
		try {
			user = this.userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("No such user");
		}
		UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
		
		return userDTO;
	}
	
	public Long login (LoginDTO loginData) throws UserNotFoundException, InvalidPasswordException {
		Optional<User> user = this.userRepository.findUserByEmail(loginData.getEmail());
		if(!user.isPresent()) {
			throw new UserNotFoundException("No such user!");
		}
		
		if(!this.checkPasswordById(loginData.getPassword(), user.get().getPassword())) {
			throw new InvalidPasswordException("Password not match");
		}
		
		return user.get().getId();
	}

//	private boolean isNullOrEmpty(String string) {
//		return string == null || string.isEmpty();
//	}

//	public void validateEmail(String email) throws InvalidEmailException {
//		if (isNullOrEmpty(email)) {
//			throw new InvalidEmailException();
//		}
//		  try {
//		      InternetAddress emailAddr = new InternetAddress(email);
//		      emailAddr.validate();
//		   } catch (AddressException ex) {
//			   throw new InvalidEmailException();
//		   }
//	}

	public void haveSameEmail(String email) throws EmailExistsException {
		if (this.userRepository.findUserByEmail(email).isPresent()) {
			throw new EmailExistsException();
		}
	}

	public void haveSameNickName(String nickName) throws UserExistsException {
		if (this.userRepository.findAll().stream().anyMatch(u -> u.getNickName().equals(nickName))) {
			throw new UserExistsException();
		}
	}

	public String encryptPassword(String password) {
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		return encryptedPassword;
	}

	public boolean checkPasswordById(String password, String encryptedPassword) {
		return BCrypt.checkpw(password, encryptedPassword);
	}

	public UserValidation getUserValidator() {
		return this.userValidator;
	}
}

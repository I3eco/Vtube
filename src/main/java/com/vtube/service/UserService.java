package com.vtube.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.vtube.dal.ChannelsRepository;
import com.vtube.dal.UsersRepository;
import com.vtube.dal.VideosRepository;
import com.vtube.dto.LoginDTO;
import com.vtube.dto.SignUpDTO;
import com.vtube.dto.UserDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.exceptions.BadCredentialsException;
import com.vtube.exceptions.ChannelNotFoundException;
import com.vtube.exceptions.EmailExistsException;
import com.vtube.exceptions.ConflictException;
import com.vtube.exceptions.UserExistsException;
import com.vtube.exceptions.UserNotFoundException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.User;
import com.vtube.model.Video;
import com.vtube.validations.UserValidation;

import lombok.NonNull;

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
	private ChannelsRepository channelRepository;

	@Autowired
	private VideosRepository videosRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserValidation userValidator;

	@Autowired
	private VideoService videoService;

	public UserDTO createUser(SignUpDTO signUpData) {
		User user = this.modelMapper.map(signUpData, User.class);

		user = this.userRepository.save(user);

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
		if (user.getLikedVideos() != null) {
			userDTO.setNumberOfLikedVideos(user.getLikedVideos().size());
		}
		if (user.getOwnedChannel() != null && user.getOwnedChannel().getOwnedVideos() != null) {
			userDTO.setNumberOfOwnVideos(user.getOwnedChannel().getOwnedVideos().size());
		}

		return userDTO;
	}

	public Long login(LoginDTO loginData) throws BadCredentialsException {
		Optional<User> user = this.userRepository.findUserByEmail(loginData.getEmail());
		if (!user.isPresent() || !this.checkPasswordById(loginData.getPassword(), user.get().getPassword())) {
			throw new BadCredentialsException("Wrong username or password");
		}

		return user.get().getId();
	}

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

	public @NonNull Optional<User> findById(Long id) {
		return this.userRepository.findById(id);
	}

	public User getUserById(Long userId) {
		return this.userRepository.findUserById(userId);
	}

	public List<VideoDTO> getUserWatchedVideos(Long userId) {
		User user = this.userRepository.findById(userId).get();

		List<Video> watchedVideos = user.getWatchedVideos();
		List<VideoDTO> watchedVideosDTO = new LinkedList<VideoDTO>();

		watchedVideos.stream()
				.forEach(video -> watchedVideosDTO.add(this.videoService.convertFromVideoToVideoDTO(video)));

		return watchedVideosDTO;
	}

	public List<VideoDTO> getUserVideosForLater(Long userId) {
		User user = this.userRepository.findById(userId).get();

		List<Video> videosForLater = user.getVideosForLater();
		List<VideoDTO> videosForLaterDTO = new LinkedList<VideoDTO>();

		videosForLater.stream()
				.forEach(video -> videosForLaterDTO.add(this.videoService.convertFromVideoToVideoDTO(video)));

		return videosForLaterDTO;
	}

	public void likeVideo(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			System.out.println("Before");
			video = this.videosRepository.findById(videoId).get();
			System.out.println("After");
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		
		//if user've already liked this video
		if(user.getLikedVideos().contains(video)) {
			return;
		}
		
		//if user've already dislike this video
		if(video.getUsersWhoDisLikeThisVideo().contains(user)) {
			video.getUsersWhoDisLikeThisVideo().remove(user);
		}
		video.getUsersWhoLikeThisVideo().add(user);
		this.videosRepository.save(video);
		
		user.getLikedVideos().add(video);
		this.userRepository.save(user);
	}

	public void removeVideoLike(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		
		//if user've already liked this video
		if(!user.getLikedVideos().contains(video)) {
			return;
		}
		
		video.getUsersWhoLikeThisVideo().remove(user);
		this.videosRepository.save(video);
		
		user.getLikedVideos().remove(video);
		this.userRepository.save(user);
		
	}

	public void dislikeVideo(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		
		//if user've already disliked this video
		if(video.getUsersWhoDisLikeThisVideo().contains(user)) {
			return;
		}
		
		//if user've already liked this video
		if(video.getUsersWhoLikeThisVideo().contains(user)) {
			video.getUsersWhoLikeThisVideo().remove(user);
			user.getLikedVideos().remove(video);
		}
		
		video.getUsersWhoDisLikeThisVideo().add(user);
		this.videosRepository.save(video);
	}
	
	public void removeVideoDislike(Long videoId, Long userId) throws VideoNotFoundException {
		Video video = null;
		
		try {
			video = this.videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		
		User user = this.userRepository.findById(userId).get();
		
		if(!video.getUsersWhoDisLikeThisVideo().contains(user)) {
			return;
		}
		
		video.getUsersWhoDisLikeThisVideo().remove(user);
		this.videosRepository.save(video);
	}

	public void subscribeToChannel(Long userId, Long channelId) throws ChannelNotFoundException, ConflictException {
		Channel channel = null;
		try {
			channel = this.channelRepository.findById(channelId).get();
		} catch (NoSuchElementException e){
			throw new ChannelNotFoundException();
		}
		
		if(channel.getOwner().getId().equals(userId)) {
			throw new ConflictException("Cannot subscribe to your own channel!");
		}
		
		User user = this.userRepository.findById(userId).get();
		
/*
 * 		In like/dislike video logic, I made one method for like on post request, and other for dislike on delete request.
		Since I wonder if it isn't better just to make one method with simple post request,
		I will implement the logic here.
 */
		if(user.getSubscribedChannels().contains(channel)) {
			user.getSubscribedChannels().remove(channel);
			channel.getUsersSubscribedToChannel().remove(user);
		} else {
			user.getSubscribedChannels().add(channel);
			channel.getUsersSubscribedToChannel().add(user);
		}
		
		this.userRepository.save(user);
		this.channelRepository.save(channel);
	}
}

package com.vtube.service;

import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.ChannnelsRepository;
import com.vtube.dal.UsersRepository;
import com.vtube.dto.ChannelDTO;
import com.vtube.exceptions.ChannelNotFoundException;
import com.vtube.exceptions.UserDoNotHaveChannelException;
import com.vtube.model.Channel;
import com.vtube.model.User;

/**
 * Class to manage database with channel related requests.
 * 
 * @author I3eco
 *
 */
@Service
public class ChannelService {
	
	@Autowired
	private ChannnelsRepository channelsRepository;
	
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private ModelMapper modelMapper;

	public void createChannel(Long userId) {
		User user = this.usersRepository.findById(userId).get();
		
		Channel channel = new Channel();
		channel.setName(user.getNickName());
		channel.setOwner(user);
		
		this.channelsRepository.save(channel);
	}

	public ChannelDTO getChannelById(Long id) throws ChannelNotFoundException {
		Channel channel = null;
		try {
			channel = this.channelsRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ChannelNotFoundException("No such channel!");
		}
		
		ChannelDTO channelDTO = this.modelMapper.map(channel, ChannelDTO.class);
		
		return channelDTO;
	}
	
	public ChannelDTO getChannelDTOByUserId(Long id) throws UserDoNotHaveChannelException {
		Channel channel = null;
		User user = this.usersRepository.findById(id).get();
		channel = user.getOwnedChannel();
		
		if(channel == null) {
			throw new UserDoNotHaveChannelException("User do not have channel");
		}
		
		ChannelDTO channelDTO = this.modelMapper.map(channel, ChannelDTO.class);
		
		return channelDTO;
	}
	
	public Channel getChannelByUserId(Long id) throws UserDoNotHaveChannelException {
		Channel channel = null;
		User user = this.usersRepository.findById(id).get();
		channel = user.getOwnedChannel();
		
		if(channel == null) {
			throw new UserDoNotHaveChannelException("User do not have channel");
		}
		
		return channel;
	}
}

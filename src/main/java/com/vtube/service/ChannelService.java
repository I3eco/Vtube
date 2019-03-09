package com.vtube.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.ChannnelsRepository;
import com.vtube.dal.UsersRepository;
import com.vtube.dto.ChannelDTO;
import com.vtube.dto.SmallVideoDTO;
import com.vtube.exceptions.ChannelNotFoundException;
import com.vtube.exceptions.UserDoNotHaveChannelException;
import com.vtube.model.Channel;
import com.vtube.model.User;
import com.vtube.model.Video;

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
		
		ChannelDTO channelDTO = this.convertFromChannelToChannelDTO(channel);
		
		return channelDTO;
	}
	
	public ChannelDTO getChannelDTOByUserId(Long id) throws UserDoNotHaveChannelException {
		Channel channel = null;
		User user = this.usersRepository.findById(id).get();
		channel = user.getOwnedChannel();
		
		if(channel == null) {
			throw new UserDoNotHaveChannelException("User do not have channel");
		}
		
		ChannelDTO channelDTO = this.convertFromChannelToChannelDTO(channel);
		
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
	
	public ChannelDTO convertFromChannelToChannelDTO(Channel parent){
		ChannelDTO channel = this.modelMapper.map(parent, ChannelDTO.class);
		
		List<Video> parentVideos = parent.getOwnedVideos();
		Type listType = new TypeToken<List<SmallVideoDTO>>(){}.getType();
		List<SmallVideoDTO> videos = this.modelMapper.map(parentVideos, listType);
		channel.setOwnedVideos(videos);
		
//		for (int videoPosition = 0; videoPosition < videos.size(); videoPosition++) {
//			videos.get(videoPosition).setNumberOfViews(parentVideos.get(videoPosition).getUsersWatchedThisVideo().size());
//		}
		
		return channel;
	}
}

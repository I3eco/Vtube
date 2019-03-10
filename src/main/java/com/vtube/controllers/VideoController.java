package com.vtube.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vtube.dto.BigVideoDTO;
import com.vtube.dto.CreatedVideoDTO;
import com.vtube.dto.Idto;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.exceptions.FileExistsException;
import com.vtube.exceptions.NoSuchVideoException;
import com.vtube.exceptions.NotLoggedInException;
import com.vtube.exceptions.UnauthorizedException;
import com.vtube.exceptions.UnsupportedFileFormatException;
import com.vtube.exceptions.UserDoNotHaveChannelException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.Video;
import com.vtube.service.ChannelService;
import com.vtube.service.SessionService;
import com.vtube.service.VideoService;

@RestController
public class VideoController {
	
	@Autowired
	VideoService videoService;
	
	@Autowired
	ChannelService channelService;
	
	@Autowired
	SessionService session;
	
	@Autowired
	private ModelMapper mapper;
	
	@PostMapping("/videos")
	@ResponseBody
	public CreatedVideoDTO uploadVideo(
			@RequestParam("file") MultipartFile file, @RequestParam("thumbnail") MultipartFile thumbnail, 
			@RequestParam(name= "title", required = false) String title, @RequestParam(name="description", required = false) String description, 
			HttpServletRequest request
			) throws NotLoggedInException, UserDoNotHaveChannelException, VideoNotFoundException, FileExistsException, UnsupportedFileFormatException {
		Long userId = this.session.getUserId(request);
		
		Channel channel = null;
		try {
			channel = this.channelService.getChannelByUserId(userId);
		} catch (UserDoNotHaveChannelException e) {
			throw new UserDoNotHaveChannelException("You do not have channel and cannot upload video!", e);
		}
		
		CreatedVideoDTO video = this.videoService.uploadVideoData(file, thumbnail, title, description, userId, channel);
		
		return video;
	}
	
	@GetMapping("/watch")
	@ResponseBody
	public BigVideoDTO watchVideoByID(@RequestParam("videoId") Long id, HttpServletRequest request) throws VideoNotFoundException {
		Long userId = null;
		try {
			userId = this.session.getUserId(request);
		} catch (NotLoggedInException e) {
			BigVideoDTO video = this.videoService.getBigVideoDTOById(id);
			return video;
		}
		
		BigVideoDTO video = this.videoService.getBigVideoDTOById(id, userId);
		
		return video;
	}
	
	
	@GetMapping("/videos/search")
	@ResponseBody
	public List<Idto> searchVideos(@RequestParam("search") String search) {
		
		List<Video> videos = this.videoService.findAllBySearchString(search);
		if (videos == null || videos.isEmpty()) {
			SimpleMessageDTO message = new SimpleMessageDTO();
			message.setMessage("No videos match your search!");
			List<Idto> messages = new LinkedList<Idto>();
			messages.add(message);
			return messages;
		}
		List<Idto> videoDTOs = new LinkedList<Idto>();
		
		for (Video video : videos) {
			VideoDTO videoDTO = new VideoDTO();
			this.mapper.map(video, videoDTO);
			videoDTOs.add(videoDTO);
		}
		return videoDTOs;
	}
	
	
	@DeleteMapping("/videos")
	@ResponseBody
	public Idto deleteVideo(@RequestParam("videoId") Long videoId, HttpServletRequest request) {
		
		if (!this.videoService.findById(videoId)) {
			try {
				throw new NoSuchVideoException("No such video!");
			} catch (NoSuchVideoException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such video!");
				return message;
			}
		}
		
		HttpSession session = request.getSession();
		if (session == null) {
			try {
				throw new NotLoggedInException("You are not logged in!");
			} catch (NotLoggedInException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("You are not logged in!");
				return message;
			}
		}
		
		Long sessionUserId = (Long) session.getAttribute("userId");
		Long videoUserId = (Long) this.videoService.getVideoById( ( (int)((long)videoId)) ).getOwner().getOwner().getId();
		if (sessionUserId != videoUserId) {
			try {
				throw new UnauthorizedException("You are allowed to delete your videos only!");
			} catch (UnauthorizedException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such video!");
				return message;
			}
		}
		
		this.videoService.deleteVideo(videoId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your video was deleted!");
		return message;
	}
	
	
	
	
	
}

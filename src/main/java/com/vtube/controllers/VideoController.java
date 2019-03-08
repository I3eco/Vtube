package com.vtube.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vtube.exceptions.FileExistsException;
import com.vtube.exceptions.UnsupportedFileFormatException;
import com.vtube.exceptions.UserDoNotHaveChannelException;
import com.vtube.exceptions.UserNotFoundException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
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
	
	@PostMapping("/videos")
	@ResponseBody
	public Long uploadVideo(
			@RequestParam("file") MultipartFile file, @RequestParam("thumbnail") MultipartFile thumbnail, 
			@RequestParam(name= "title", required = false) String title, @RequestParam(name="description", required = false) String description, 
			HttpServletRequest request
			) throws UserNotFoundException, UserDoNotHaveChannelException, VideoNotFoundException, FileExistsException, UnsupportedFileFormatException {
		Long userId = this.session.getUserId(request);
		
		Channel channel = null;
		try {
			channel = this.channelService.getChannelByUserId(userId);
		} catch (UserDoNotHaveChannelException e) {
			throw new UserDoNotHaveChannelException("You do not have channel and cannot upload video!", e);
		}
		
		this.videoService.uploadVideoData(file, thumbnail, title, description, userId, channel);
		
		return (long) 1;
	}
	
}

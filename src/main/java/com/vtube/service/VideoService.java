package com.vtube.service;

import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.VideosRepository;
import com.vtube.model.Video;

/**
 * Class to manage database with video related requests.
 * 
 * @author I3eco
 *
 */
@Service
public class VideoService {
	@Autowired
	private VideosRepository videosRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public boolean findById(Integer videoId) {
		try {
			Video video = videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
}

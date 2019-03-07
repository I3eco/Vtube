package com.vtube.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.VideosRepository;

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
	
}

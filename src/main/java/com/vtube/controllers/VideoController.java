package com.vtube.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.service.VideoService;

@RestController
public class VideoController {
	
	@Autowired VideoService videoService;
}

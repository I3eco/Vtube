package com.vtube.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vtube.dal.CommentsRepository;
import com.vtube.model.Comment;

public class CommentService {

	@Autowired
	CommentsRepository commentsRepository;
	public List<Comment> findAllByVideoId(Integer videoId) {
		return commentsRepository.findAllByVideoId(videoId);
	}
	

	
}

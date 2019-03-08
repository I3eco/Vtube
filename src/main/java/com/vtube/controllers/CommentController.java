package com.vtube.controllers;

import java.util.LinkedList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.dto.CommentDTO;
import com.vtube.exceptions.NoSuchVideoException;
import com.vtube.model.Comment;
import com.vtube.service.CommentService;
import com.vtube.service.UserService;
import com.vtube.service.VideoService;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserService userService;
	
	@GetMapping("/comments")
	public List<CommentDTO> getCommentsByVideo(@RequestParam("videoId") Integer videoId) throws NoSuchVideoException {
		
		if (!this.videoService.findById(videoId)) {
			throw new NoSuchVideoException("No such video!");
		}
		
		List<Comment> comments = this.commentService.findAllByVideoId(videoId);
		List<CommentDTO> commentDTOs = new LinkedList<CommentDTO>();
		
		for (Comment comment : comments) {
			CommentDTO commentDTO = new CommentDTO();
			this.mapper.map(comment, commentDTO);
			commentDTO.setUserNickName(userService.findById(comment.getAuthor().getId()).get().getNickName());
			commentDTOs.add(commentDTO);
		}
		return commentDTOs;
	}
	
	
	
}

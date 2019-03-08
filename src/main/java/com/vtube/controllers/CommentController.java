package com.vtube.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vtube.dto.CommentDTO;
import com.vtube.dto.Idto;
import com.vtube.dto.SimpleMessageDTO;
import com.vtube.exceptions.NoSuchCommentException;
import com.vtube.exceptions.NoSuchVideoException;
import com.vtube.exceptions.NotLoggedInException;
import com.vtube.model.Comment;
import com.vtube.model.User;
import com.vtube.model.Video;
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
	@ResponseBody
	public List<Idto> getCommentsByVideo(@RequestParam("videoId") Integer videoId) {
		
		if (!this.commentService.findById(videoId)) {
			try {
				throw new NoSuchVideoException("No such video!");
			} catch (NoSuchVideoException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such video!");
				List<Idto> messages = new LinkedList<Idto>();
				messages.add(message);
				return messages;
			}
		}
		
		List<Comment> comments = this.commentService.findAllByVideoId(videoId);
		List<Idto> commentDTOs = new LinkedList<Idto>();
		
		for (Comment comment : comments) {
			CommentDTO commentDTO = new CommentDTO();
			this.mapper.map(comment, commentDTO);
			commentDTO.setUserNickName(userService.findById(comment.getAuthor().getId()).get().getNickName());
			List<Comment> subComments = this.commentService.findAllByCommentId( (int)((long)comment.getId()) );
			for (Comment c : subComments) {
				CommentDTO subCommentDTO = new CommentDTO();
				this.mapper.map(c, subCommentDTO);
				subCommentDTO.setUserNickName(userService.findById(c.getAuthor().getId()).get().getNickName());
				subCommentDTO.add(subCommentDTO);
			}
			commentDTOs.add(commentDTO);
		}
		return commentDTOs;
	}
	
	
	@GetMapping("/commentReplies")
	@ResponseBody
	public List<Idto> getCommentsBySupercomment(@RequestParam("commentId") Integer commentId) {
		
		if (!this.commentService.findById(commentId)) {
			try {
				throw new NoSuchCommentException("No such comment!");
			} catch (NoSuchCommentException e) {
				e.printStackTrace();
				SimpleMessageDTO message = new SimpleMessageDTO();
				message.setMessage("No such comment!");
				List<Idto> messages = new LinkedList<Idto>();
				messages.add(message);
				return messages;
			}
		}
		
		List<Comment> comments = this.commentService.findAllByCommentId(commentId);
		List<Idto> commentDTOs = new LinkedList<Idto>();
		
		for (Comment comment : comments) {
			CommentDTO commentDTO = new CommentDTO();
			this.mapper.map(comment, commentDTO);
			commentDTO.setUserNickName(userService.findById(comment.getAuthor().getId()).get().getNickName());
			commentDTO.setSuperCommentId(comment.getSuperComment().getId());
			commentDTOs.add(commentDTO);
		}
		return commentDTOs;
	}
	
	
	@PostMapping("/comments")
	@ResponseBody
	public Idto addComment(@RequestParam("videoId") Integer videoId,
			@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
		
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
		
		Long userId = (Long) session.getAttribute("userId");
		User author = this.userService.getUserById(userId);
		Video video = this.videoService.getVideoById(videoId);
		Comment comment = new Comment(commentDTO.getId(), commentDTO.getContent(), 0, 0, null, author, video);
		this.commentService.addComment(commentDTO, userId, videoId);
		SimpleMessageDTO message = new SimpleMessageDTO();
		message.setMessage("Your comment was added!");
		return message;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

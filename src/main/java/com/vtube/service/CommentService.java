package com.vtube.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vtube.dal.CommentsRepository;
import com.vtube.dto.CommentDTO;
import com.vtube.model.Comment;
import com.vtube.model.User;
import com.vtube.model.Video;

@Service
public class CommentService {

	@Autowired
	CommentsRepository commentsRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VideoService videoService;
	
	public List<Comment> findAllByVideoId(Integer videoId) {
		return commentsRepository.findAllByVideoId(videoId);
	}
	public boolean findById(Integer commentId) {
		try {
			Comment comment = this.commentsRepository.findById(commentId).get();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}
	public List<Comment> findAllByCommentId(Integer commentId) {
		return commentsRepository.findAllByCommentId(commentId);
	}
	
	public void addComment(CommentDTO commentDTO, Long userId, Integer videoId) {
		User author = this.userService.getUserById(userId);
		Video video = this.videoService.getVideoById(videoId);
		Comment comment = new Comment(commentDTO.getId(), commentDTO.getContent(), 0, 0, null, author, video);	
		this.commentsRepository.save(comment);
	}
	

	
}

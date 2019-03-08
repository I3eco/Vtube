//package com.vtube.service;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.vtube.dal.CommentsRepository;
//import com.vtube.model.Comment;
//import com.vtube.model.Video;
//
//@Service
//public class CommentService {
//
//	@Autowired
//	CommentsRepository commentsRepository;
//	
//	public List<Comment> findAllByVideoId(Long videoId) {
//		return commentsRepository.findAllByVideoId(videoId);
//	}
//	public boolean findById(Integer commentId) {
//		try {
//			Comment comment = this.commentsRepository.findById(commentId).get();
//		} catch (NoSuchElementException e) {
//			return false;
//		}
//		return true;
//	}
//	public List<Comment> findAllByCommentId(Integer commentId) {
//		return commentsRepository.findAllByCommentId(commentId);
//	}
//	
//
//	
//}

//package com.vtube.controllers;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.vtube.dto.CommentDTO;
//import com.vtube.exceptions.NoSuchVideoException;
//import com.vtube.model.Comment;
//import com.vtube.service.CommentService;
//import com.vtube.service.UserService;
//import com.vtube.service.VideoService;
//
//@RestController
//public class CommentController {
//
//	@Autowired
//	private CommentService commentService;
//	
//	@Autowired
//	private VideoService videoService;
//	
//	@Autowired
//	private ModelMapper mapper;
//
//	@Autowired
//	private UserService userService;
//	
//	@GetMapping("/comments")
//	public List<CommentDTO> getCommentsByVideo(@RequestParam("videoId") Long videoId) throws NoSuchVideoException {
//		
//		if (!this.videoService.findById(videoId)) {
//			throw new NoSuchVideoException("No such video!");
//		}
//		
//		List<Comment> comments = this.commentService.findAllByVideoId(videoId);
//		List<CommentDTO> commentDTOs = new LinkedList<CommentDTO>();
//		
//		for (Comment comment : comments) {
//			CommentDTO commentDTO = new CommentDTO();
//			this.mapper.map(comment, commentDTO);
//			commentDTO.setUserNickName(userService.findById(comment.getAuthor().getId()).get().getNickName());
//			List<Comment> subComments = this.commentService.findAllByCommentId( (int)((long)comment.getId()) );
//			for (Comment c : subComments) {
//				CommentDTO subCommentDTO = new CommentDTO();
//				this.mapper.map(c, subCommentDTO);
//				subCommentDTO.setUserNickName(userService.findById(c.getAuthor().getId()).get().getNickName());
//				subCommentDTO.add(subCommentDTO);
//			}
//			commentDTOs.add(commentDTO);
//		}
//		return commentDTOs;
//	}
//	
//	@GetMapping("/commentReplies")
//	public List<CommentDTO> getCommentsBySupercomment(@RequestParam("commentId") Integer commentId) throws NoSuchVideoException {
//		
//		if (!this.commentService.findById(commentId)) {
//			throw new NoSuchVideoException("No such video!");
//		}
//		
//		List<Comment> comments = this.commentService.findAllByCommentId(commentId);
//		List<CommentDTO> commentDTOs = new LinkedList<CommentDTO>();
//		
//		for (Comment comment : comments) {
//			CommentDTO commentDTO = new CommentDTO();
//			this.mapper.map(comment, commentDTO);
//			commentDTO.setUserNickName(userService.findById(comment.getAuthor().getId()).get().getNickName());
//			commentDTO.setSuperCommentId(comment.getSuperComment().getId());
//			commentDTOs.add(commentDTO);
//		}
//		return commentDTOs;
//	}
//	
//	
//}

package com.vtube.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.Comment;
import com.vtube.model.Video;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long>{

	List<Comment> findAllByVideoId(Integer videoId);

	Optional<Comment> findById(Integer commentId);

	List<Comment> findAllByCommentId(Integer commentId);

	Video findVideoByCommentId(Integer commentId);

	
}
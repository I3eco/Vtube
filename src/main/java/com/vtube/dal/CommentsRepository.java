package com.vtube.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.Comment;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long>{
	
}
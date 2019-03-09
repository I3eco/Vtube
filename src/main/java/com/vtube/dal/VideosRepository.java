package com.vtube.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.Video;

@Repository
public interface VideosRepository extends JpaRepository<Video, Long>{

	Optional<Video> findById(Integer videoId);
	
	
}

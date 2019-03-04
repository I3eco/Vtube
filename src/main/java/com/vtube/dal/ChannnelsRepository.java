package com.vtube.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.Channel;

@Repository
public interface ChannnelsRepository extends JpaRepository<Channel, Long>{
	
}

package com.vtube.dal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vtube.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>{
	User findUserByEmail(String email);
	
	//TODO make method which will return all subscribers of the user channel if exists
//	@Query("select s.user_id \r\n" + 
//			"from subscribed_channels s \r\n" + 
//			"join Channel c on (s.channel_id = c.id) \r\n" + 
//			"join User u on (c.user_id = u.id)\r\n" + 
//			"where u.id = :id")
//	List<Long> findAllSubscribers(@Param("id") Long id);
}
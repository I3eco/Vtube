package com.vtube.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>{
	Optional<User> findUserByEmail(String email);
	Optional<User> findUserByNickName(String nickName);
	User findUserById(Long userId);
	
	//TODO make method which will return all subscribers of the user channel if exists
//	@Query("SELECT c.usersSubscribedToChannel")
//	List<Long> findAllSubscribers(@Param("id") Long id);
}
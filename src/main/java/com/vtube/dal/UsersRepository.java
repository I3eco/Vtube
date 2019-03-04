package com.vtube.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtube.model.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>{
	
}
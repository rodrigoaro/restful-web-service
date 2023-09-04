package com.in28minutes.restfulwebservices.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in28minutes.restfulwebservices.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	

}

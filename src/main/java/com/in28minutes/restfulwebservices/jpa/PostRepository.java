package com.in28minutes.restfulwebservices.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in28minutes.restfulwebservices.user.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
	
	

}

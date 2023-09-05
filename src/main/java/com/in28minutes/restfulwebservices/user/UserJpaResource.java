package com.in28minutes.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.restfulwebservices.jpa.PostRepository;
import com.in28minutes.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	
	private UserRepository userRepository;
	
	private PostRepository postRepository;
	
	public UserJpaResource(UserRepository userRepository,PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@GetMapping(path = "/jpa/users")
	public List<User> retrieveAllUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping(path = "/jpa/users/{idUser}")
	public EntityModel<User> retrieveUserById(@PathVariable Integer idUser){
		Optional<User> user = userRepository.findById(idUser);
		if(user.isEmpty()) {
			throw new UserNotFoundException("idUser: "+ idUser);
		}
		EntityModel<User> entityModel = EntityModel.of(user.get());
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}
	
	@PostMapping(path = "/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User userSaved = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(userSaved.getId())
						.toUri();

		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path = "/jpa/users/{idUser}")
	public void deleteUser(@PathVariable Integer idUser){
		userRepository.deleteById(idUser);
	}
	
	@GetMapping(path = "/jpa/users/{idUser}/posts")
	public List<Post> retrievePostsForAUser(@PathVariable Integer idUser){

		Optional<User> user = userRepository.findById(idUser);
		if(user.isEmpty()) {
			throw new UserNotFoundException("idUser: "+ idUser);
		}
		
		return user.get().getPosts();
	}
	
	@PostMapping(path = "/jpa/users/{idUser}/posts")
	public ResponseEntity<Post> createPostForUser(@PathVariable Integer idUser, @Valid @RequestBody Post post) {
		
		Optional<User> user = userRepository.findById(idUser);
		if(user.isEmpty()) {
			throw new UserNotFoundException("idUser: "+ idUser);
		}
		
		post.setUser(user.get());
		
		Post postSaved = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(postSaved.getId())
						.toUri();

		return ResponseEntity.created(location).build();
	}
	
	@GetMapping(path = "/jpa/users/{idUser}/posts/{idPost}")
	public EntityModel<Post> retrieveUserPostById(@PathVariable Integer idUser, @PathVariable Integer idPost){
		Optional<User> user = userRepository.findById(idUser);
		if(user.isEmpty()) {
			throw new UserNotFoundException("idUser: "+ idUser);
		}
		Optional<Post> post = postRepository.findById(idPost);
		if(post.isEmpty()) {
			throw new PostNotFoundException("idPost: "+ idPost);
		}
		
		EntityModel<Post> entityModel = EntityModel.of(post.get());
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrievePostsForAUser(idUser));
		entityModel.add(link.withRel("all-posts"));
		return entityModel;
	}

}

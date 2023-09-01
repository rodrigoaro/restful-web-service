package com.in28minutes.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResource {
	
	private UserDaoService userDaoService;
	
	public UserResource(UserDaoService userDaoService) {
		this.userDaoService = userDaoService;
	}

	@GetMapping(path = "/users")
	public List<User> retrieveAllUsers(){
		return userDaoService.findAllUsers();
	}
	
	@GetMapping(path = "/users/{idUser}")
	public User retrieveUserById(@PathVariable Integer idUser){
		User user = userDaoService.findUserById(idUser);
		if(user == null) {
			throw new UserNotFoundException("idUser: "+ idUser);
		}
		return user;
	}
	
	@PostMapping(path = "/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User userSaved = userDaoService.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(userSaved.getId())
						.toUri();

		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path = "/users/{idUser}")
	public void deleteUser(@PathVariable Integer idUser){
		userDaoService.deleteUser(idUser);
	}

}

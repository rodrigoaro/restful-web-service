package com.in28minutes.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {

	private static List<User> users = new ArrayList<>();
	
	private static Integer usersCount = 0;
	
	static {
		users.add(new User(++usersCount, "Rod", LocalDate.now().minusYears(40)));
		users.add(new User(++usersCount, "Ingrid", LocalDate.now().minusYears(33)));
		users.add(new User(++usersCount, "Franco", LocalDate.now().minusYears(18)));
	}
	
	public List<User> findAllUsers(){
		return users;
	}

	public User findUserById(Integer idUser) {
		Predicate<? super User> predicate = user -> user.getId().equals(idUser);
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
	
	public User save(User user) {
		user.setId(++usersCount);
		users.add(user);
		return user;
	}
	
	public void deleteUser(Integer idUser) {
		Predicate<? super User> predicate = user -> user.getId().equals(idUser);
		users.removeIf(predicate);
	}
	
	
}

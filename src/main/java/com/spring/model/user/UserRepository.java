package com.spring.model.user;

import org.springframework.data.repository.CrudRepository;
public interface UserRepository extends CrudRepository<User, String>{
	
	public User findByUsername(String name);
    public User save(User user);


}
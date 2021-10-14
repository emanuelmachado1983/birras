package com.emanuel.birras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.emanuel.birras.model.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	User findByUserBeer(String userBeer);
	User findByEmail(String email);	
	List<User> findAllByOrderByName();
	
	@Modifying
	@Transactional
	@Query("update User u set u.email = :email, u.name = :name where u.id = :id")
	void updateUserEmail(@Param(value = "id") long id, @Param(value = "email") String email, @Param(value = "name") String name);
	
	@Modifying
	@Transactional
	@Query("update User u set u.password = :password where u.userBeer = :userBeer")
	void updateUserPassword(@Param(value = "userBeer") String id, @Param(value = "password") String password);
}

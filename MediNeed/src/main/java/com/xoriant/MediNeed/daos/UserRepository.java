package com.xoriant.MediNeed.daos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.MediNeed.model.Users;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer>{

	public Users findByEmail(String email);
	
	@Query("select u from Users u")
	public List<Users> getAllUsers();

//	@Query("select u from Users u where u.fname =:n")
//	public Users getUserByFirstName(@Param("n") String fname);
	
	@Query(value = "select * from user where fname =?1", nativeQuery = true)
	public Users getUserByFirstName(String fname);

}

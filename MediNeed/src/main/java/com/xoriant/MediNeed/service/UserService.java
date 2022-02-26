package com.xoriant.MediNeed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.MediNeed.daos.UserRepository;
import com.xoriant.MediNeed.model.Users;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public Users findByEmailId(String email) {
		return userRepo.findByEmail(email);
	}

	public Users createUser(Users users) {
		return userRepo.save(users);
	}

	public String getUploadDirectory(String email) {
		return userRepo.findByEmail(email).getUPLOAD_DIRECTORY();
	}
	
	public List<Users> getUsers() {
		return userRepo.getAllUsers();
	}
}

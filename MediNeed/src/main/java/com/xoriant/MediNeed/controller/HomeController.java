package com.xoriant.MediNeed.controller;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.xoriant.MediNeed.helper.FileUploadHelper;
import com.xoriant.MediNeed.helper.Message;
import com.xoriant.MediNeed.model.*;
import com.xoriant.MediNeed.service.UserService;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private FileUploadHelper fileUploadHelper;
	
	@RequestMapping(value = { "/", "/home" } )
	public String getHomePage(Model model) {
		model.addAttribute("title", "Home - MediNeed");
		return "home";
	}

	@RequestMapping("/signin")
	public String signInPage(Model model) {
		model.addAttribute("title", "MediNeed - Sign In");
		return "signin";
	}
	
	@RequestMapping("/about")
	public String aboutPage(Model model) {
		model.addAttribute("title", "About");
		return "about";
	}

	@RequestMapping("/signup")
	public String signUpPage(Model model) {
		model.addAttribute("title", "MediNeed - Sign Up");
		model.addAttribute("users", new Users());
		return "signup";
	}
	
	@RequestMapping("/file-upload")
	public String fileUploadPage(Model model) {
		model.addAttribute("title", "MediNeed - Sign Up");
		return "file-upload";
	}
	
	@PostMapping(value = "/do_login")
	public String logginUser(@ModelAttribute("users") Users users, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password, Model model, HttpSession session) {
		
		model.addAttribute("title", "Login-Home MediNeed");
		Users u1 = userServ.findByEmailId(email);
		
		try {
			if(u1==null) {
				throw new Exception("Invalid Credentials...!");
			}
			
			if(!u1.getPassword().equals(password)) {
				throw new Exception("Invalid Credentials...!");
			}
			model.addAttribute("fname", u1.getFname());
			model.addAttribute("lname", u1.getLname());
			System.out.println("Successfully Logged-In");
			
			fileUploadHelper.setFULL_PATH(u1.getUPLOAD_DIRECTORY());

			return "login-home";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "signin";
		}
	}

	// This is a handler for user registration
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("users") Users users,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "confirmpassword") String confirmpassword,
			@RequestParam(value = "about") String about,
			Model model, HttpSession session) {

		try {
			if (!agreement) {
				throw new Exception("You have not agreed the terms and conditions...!");
			}

			if (!password.equals(confirmpassword)) {
				throw new Exception("Your password doesnot match...!");
			}

			users.setImgUrl("default.png");
			users.setEnabled(false);
			users.setRole("ROLE_USER");
			users.setAbout(about);
		
			File f = new File(fileUploadHelper.getUPLOAD_DIR() + File.separator + users.getFname() + " " + users.getLname());
			System.out.println(f.getAbsolutePath());
			users.setUPLOAD_DIRECTORY(f.getAbsolutePath());
			
			Users u1 = userServ.createUser(users);
			model.addAttribute("users", u1);
			session.setAttribute("message", new Message("Successfully registered...!", "alert-success"));
			
			f.mkdir();
			return "signup";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			model.addAttribute("users", users);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	
	//Handler for file upload
		@PostMapping("/do-upload")
	    public String fileUpload(Users users, @RequestParam("file") MultipartFile file, HttpSession session) {
	        try {
	            if (!file.isEmpty()) {
	                if (file.getContentType().equals("application/pdf")) {
	                    fileUploadHelper.fileUploadHelper(file);
	                    session.setAttribute("message", new Message("Successfully Uploaded...!", "alert-success"));
	                }
	                else {
	                	session.setAttribute("message", new Message("Only pdfs are accepted...!", "alert-danger"));
	                }
	            }
	            else {
	            	session.setAttribute("message", new Message("File must atleast have content of 1 KB...!", "alert-danger"));
	            }
	            return "login-home";
	            
	        } catch (Exception e) {
	        	session.setAttribute("message", new Message(e.getMessage(), "alert-danger")); 
	        	return "login-home";
	        }
	    }
}

package com.vube.buildMySetup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vube.buildMySetup.model.User;
import com.vube.buildMySetup.repository.UserRepo;
import com.vube.buildMySetup.service.Emailservice;

@Controller
public class RegisterController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Emailservice emailService;

	// show register page
	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	@GetMapping("/check-username")
	@ResponseBody
	public String checkUsername(@RequestParam String name) {

		name = name.trim();

		boolean exists = userRepo.existsByName(name);

		System.out.println(exists);

		return exists ? "EXISTS" : "AVAILABLE";
	}

	// save user
	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, Model model) {

		// encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// default role
		user.setRole("USER");

		// save user
		userRepo.save(user);

		// send mail
		emailService.sendMail(user.getEmail(), "Welcome to Build My Setup", "Registration Successful");

		model.addAttribute("success", true);

		return "register";
	}
}
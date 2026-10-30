package com.vube.buildMySetup.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.vube.buildMySetup.customerUserDetailsservice.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	public SecurityConfig(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	// MAIN SECURITY CONFIG
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				// attach authentication provider
				.authenticationProvider(authenticationProvider())

				//  URL security rules
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/", "/home", "/login", "/register", "/css/**", "/images/**","/check-username").permitAll()
						            .requestMatchers("/admin/**").hasRole("ADMIN")
						            .requestMatchers("/user/**")
								    .hasRole("USER").anyRequest().authenticated())

				// login configuration
				.formLogin(form -> form.loginPage("/login") // custom login page
						.loginProcessingUrl("/login") // form action URL
						.usernameParameter("email") // your input name
						.passwordParameter("password").defaultSuccessUrl("/components", true) // after login → home
						.failureUrl("/login?error=true") // if login fails
						.permitAll()).exceptionHandling(ex -> ex
							    .accessDeniedPage("/access-denied")
								)

				// 👉 logout configuration
				.logout(logout -> logout.logoutSuccessUrl("/") // after logout → home
						.permitAll());

		return http.build();
	}

	//  Authentication provider
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	//  Password encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
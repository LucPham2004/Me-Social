package com.me_social.MeSocial.configuration;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.modal.Role;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.enums.Gender;
import com.me_social.MeSocial.mapper.UserMapper;
import com.me_social.MeSocial.repository.RoleRepository;
import com.me_social.MeSocial.repository.UserRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {
    
    @Bean
	CommandLineRunner run(	RoleRepository roleRepository, UserRepository userRepository, UserMapper mapper, 
							PasswordEncoder passwordEncoder, EntityManager entityManager) {
		return args -> {
			entityManager.clear();
			Role adminRole = roleRepository.findByAuthority("ADMIN").orElse(null);
			Role userRole = roleRepository.findByAuthority("USER").orElse(null);
	
			if (adminRole == null) {
				adminRole = roleRepository.save(new Role("ADMIN"));
			}
			if (userRole == null) {
				userRole = roleRepository.save(new Role("USER"));
			}
	
			if (!userRepository.existsByUsername("ADMIN")) {
				Set<Role> roles = new HashSet<>();
				roles.add(adminRole);
				roles.add(userRole);

                UserCreationRequest request = new UserCreationRequest();

                request.setUsername("ADMIN");
                request.setEmail("ahahaha@gmail.com");
                request.setPhone("0838699999");
                request.setFirstName("ADMIN");
                request.setGender(Gender.Male);
                request.setDob(Instant.now());
                request.setPassword("adminPassword");

                User admin = mapper.toUser(request);
                admin.setPassword(passwordEncoder.encode(request.getPassword()));
                admin.setAuthorities(roles);

				userRepository.save(admin);
			}
		};
	}
}

package com.me_social.MeSocial.configuration.security;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class UserDetailsCustom implements UserDetailsService {

     // private final UserService userService;
     private final UserRepository userRepository;

     @Override
     public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
          // find user in database
          // = this.userService.handleGetUserByUsername(username);
          Optional<User> userOptional = userRepository.findByUsername(login);
          log.info("username not empty");

          if (userOptional.isEmpty()) {
               log.info("username empty");
               userOptional = userRepository.findByEmail(login);
          }

          if (userOptional.isEmpty()) {
               log.info("email empty");
               userOptional = userRepository.findByPhone(login);
          }
          if (userOptional.isEmpty()) {
               // Nếu không tìm thấy user ở tất cả các trường hợp, ném ngoại lệ tại đây
               log.info("phone empty");
               throw new UsernameNotFoundException("User not found with login: " + login);
          }

          // Lấy user từ Optional nếu có
          User user = userOptional.get();
          
          return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
     }

}

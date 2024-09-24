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

     //private final UserService userService;
     private final UserRepository userRepository;

     @Override
     public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
          // find user in database
          // = this.userService.handleGetUserByUsername(username);
          log.debug("hello");
          Optional<User> userOptional = userRepository.findByUsername(login);

          if (userOptional.isEmpty()) {
               userOptional = userRepository.findByEmail(login);
          }

          if (userOptional.isEmpty()) {
               userOptional = userRepository.findByPhone(login);
          }
          if (userOptional.isEmpty()) {
               // Nếu không tìm thấy user ở tất cả các trường hợp, ném ngoại lệ tại đây
               throw new UsernameNotFoundException("User not found with login: " + login);
          }

          // Lấy user từ Optional nếu có
          User user = userOptional.get();

          // if (userOptional == null) {
          // // check if user is not in database
          // throw new UsernameNotFoundException("Username/Password is not valid");
          // }

          // return a custom user(userDetails) with the information collected in database
          // and assign to it a user role
          return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
     }

}

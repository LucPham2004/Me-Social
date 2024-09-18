package com.me_social.MeSocial.configuration.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.me_social.MeSocial.service.UserService;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {

     private final UserService userService;

     public UserDetailsCustom(UserService userService) {
          this.userService = userService;
     }

     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          // find user in database
          com.me_social.MeSocial.entity.modal.User user = this.userService.handleGetUserByUsername(username);
          if (user == null) {
               // check if user is not in database
               throw new UsernameNotFoundException("Username/Password is not valid");
          }
          // return a custom user(userDetails) with the information collected in database and assign to it a user role
          return new User(
               user.getEmail(),
               user.getPassword(),
               Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
          );
          
     }
     
}


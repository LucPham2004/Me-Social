package com.me_social.MeSocial.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.me_social.MeSocial.entity.modal.Role;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginResponse {
     @JsonProperty("access_token")
     private String access_token;
     private UserLogin user;

     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserLogin {
          private long id;
          private String email;
          private String username;
          private String locatation;
          private String bio;
          private Set<Role> authorities;
     }

     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserGetAccount {
          private UserLogin user;
     }

     @Data
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserInsideToken {
          private long id;
          private String email;
          private String username;
          private String location;
     }
}

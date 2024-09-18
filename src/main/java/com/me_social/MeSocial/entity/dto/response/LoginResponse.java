package com.me_social.MeSocial.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.me_social.MeSocial.entity.modal.Role;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class LoginResponse {
     @JsonProperty("access_token")
     private String access_token;
     private UserLogin user;

     @Getter
     @Setter
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserLogin {
          private long id;
          private String email;
          private String name;
          private Set<Role> authorities;
     }

     @Getter
     @Setter
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserGetAccount {
          private UserLogin user;
     }

     @Getter
     @Setter
     @AllArgsConstructor
     @NoArgsConstructor
     public static class UserInsideToken {
          private long id;
          private String email;
          private String name;
     }
}

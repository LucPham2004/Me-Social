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
          private String location;
          private String bio;
          private int postNum;
          private int likeNum;
          private boolean isActive;
          private Set<Role> authorities;
          private String firstName;
          private String lastName;
          private String avatarUrl;

          public UserLogin(Long id, String email, String username, String location, String bio, int postNum2,
                    int likeNum2, boolean isActive, Set<Role> authorities, String firstName, String lastName, String avatarUrl) {
               this.id = id;
               this.email = email;
               this.username = username;
               this.location = location;
               this.bio = bio;
               this.postNum = postNum2;
               this.likeNum = likeNum2;
               this.isActive = isActive;
               this.authorities = authorities;
               this.firstName = firstName;
               this.lastName = lastName;
               this.avatarUrl = avatarUrl;
          }
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

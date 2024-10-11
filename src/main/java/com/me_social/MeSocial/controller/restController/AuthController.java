package com.me_social.MeSocial.controller.restController;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.LoginRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.LoginResponse;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.service.UserService;
import com.me_social.MeSocial.utils.SecurityUtils;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

     AuthenticationManagerBuilder authenticationManagerBuilder;
     UserService userService;
     SecurityUtils securityUtils;

     @PostMapping("/login")
     public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());

          log.info("Attempting to authenticate user: {}", loginRequest.getUsername());

          // xác thực người dùng => cần viết hàm loadUserByUsername
          Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

          // create a token
          // nạp thông tin (nếu xử lý thành công) vào SecurityContext
          SecurityContextHolder.getContext().setAuthentication(authentication);
          log.info("authentication principal is null {}", authentication.getPrincipal());

          LoginResponse loginResponse = new LoginResponse();

          User currentUserDB = userService.handleGetUserByUsernameOrEmailOrPhone(loginRequest.getUsername());

          if (currentUserDB != null) {
               LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                         currentUserDB.getId(),
                         currentUserDB.getEmail(),
                         currentUserDB.getUsername(),
                         currentUserDB.getLocation(),
                         currentUserDB.getBio(),
                         currentUserDB.getAuthorities());
               loginResponse.setUser(userLogin);
          }

          String access_token = securityUtils.createAccessToken(authentication.getName(), loginResponse);
          loginResponse.setAccess_token(access_token);

          ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();
          apiResponse.setCode(1000);
          apiResponse.setMessage("Login successfully");
          apiResponse.setResult(loginResponse);

          return apiResponse;
     }

     @GetMapping("/account")
     public ApiResponse<LoginResponse.UserGetAccount> getAccount() {
          String login = SecurityUtils.getCurrentUserLogin().isPresent()
                    ? SecurityUtils.getCurrentUserLogin().get()
                    : "";

          User currentUserDB = this.userService.handleGetUserByUsernameOrEmailOrPhone(login);
          LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin();
          LoginResponse.UserGetAccount userGetAccount = new LoginResponse.UserGetAccount();
          if (currentUserDB != null) {
               userLogin.setId(currentUserDB.getId());
               userLogin.setEmail(currentUserDB.getEmail());
               userLogin.setUsername(currentUserDB.getUsername());
               userLogin.setLocatation(currentUserDB.getLocation());
               userLogin.setBio(currentUserDB.getBio());
               userLogin.setAuthorities(currentUserDB.getAuthorities());

               userGetAccount.setUser(userLogin);
          }

          return ApiResponse.<LoginResponse.UserGetAccount>builder()
                    .code(1000)
                    .message("Get current user successfully!")
                    .result(userGetAccount)
                    .build();
     }
}

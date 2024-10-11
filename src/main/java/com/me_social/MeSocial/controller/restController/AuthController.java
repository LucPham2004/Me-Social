package com.me_social.MeSocial.controller.restController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.me_social.MeSocial.entity.dto.request.LoginRequest;
import com.me_social.MeSocial.entity.dto.request.UserCreationRequest;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;
import com.me_social.MeSocial.entity.dto.response.LoginResponse;
import com.me_social.MeSocial.entity.dto.response.UserResponse;
import com.me_social.MeSocial.entity.modal.User;
import com.me_social.MeSocial.exception.AppException;
import com.me_social.MeSocial.exception.ErrorCode;
import com.me_social.MeSocial.mapper.UserMapper;
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
     PasswordEncoder passwordEncoder;
     UserMapper userMapper;

     // @Value("${me_social.jwt.refresh-token-validity-in-seconds}")
     // private Long refreshTokenExpiration;

     @PostMapping("/login")
     public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {

          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());

          log.info("Attempting to authenticate user: {}", loginRequest.getUsername());

          // Authenticate the user
          Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
          SecurityContextHolder.getContext().setAuthentication(authentication);

          log.info("authentication principal: {}", authentication.getPrincipal());

          // Prepare the login response
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

          // Generate tokens
          String access_token = securityUtils.createAccessToken(authentication.getName(), loginResponse);
          loginResponse.setAccess_token(access_token);

          String refresh_token = securityUtils.createRefreshToken(loginRequest.getUsername(), loginResponse);

          // Update refresh token for the user in the database
          userService.updateUserToken(refresh_token, loginRequest.getUsername());

          // Set refresh token as an HTTP-only cookie
          ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(securityUtils.refreshTokenExpiration)
                    .build();

          // Create the ApiResponse
          ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                    .code(1000)
                    .message("Login successfully")
                    .result(loginResponse)
                    .build();

          // Build the response with the body and headers
          return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, resCookie.toString()) // Set the cookie in the response header
                    .body(apiResponse); // Return the body with the API response
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

     @GetMapping("/refresh")
     public ResponseEntity<ApiResponse<LoginResponse>> getRefreshToken(
               @CookieValue(name = "refresh_token", defaultValue = "blabla") String refresh_token) {
          if (refresh_token.equals("blabla")) {
               throw new AppException(ErrorCode.NO_REFRESH_TOKEN);
          }
          // check valid
          Jwt decodedToken = this.securityUtils.checkValidRefreshToken(refresh_token);
          String emailUsernamePhone = decodedToken.getSubject();

          // check user by token + email
          User currentUser = this.userService.getUserByRefreshTokenAndEmailOrUsernameOrPhone(refresh_token,
                    emailUsernamePhone);

          // issue new token / set refresh token as cookies
          LoginResponse res = new LoginResponse();

          LoginResponse.UserLogin userLogin = new LoginResponse.UserLogin(
                    currentUser.getId(),
                    currentUser.getEmail(),
                    currentUser.getUsername(),
                    currentUser.getLocation(),
                    currentUser.getBio(),
                    currentUser.getAuthorities());
          res.setUser(userLogin);

          String access_token = this.securityUtils.createAccessToken(emailUsernamePhone, res);

          res.setAccess_token(access_token);

          // create refresh token
          String new_refresh_token = this.securityUtils.createRefreshToken(emailUsernamePhone, res);

          // update refreshToken for user
          this.userService.updateUserToken(new_refresh_token, emailUsernamePhone);

          // set cookies
          ResponseCookie resCookie = ResponseCookie
                    .from("refresh_token", new_refresh_token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(securityUtils.refreshTokenExpiration)
                    .build();

          ApiResponse<LoginResponse> apiResponse = new ApiResponse<>();

          apiResponse.setCode(1000);
          apiResponse.setMessage("get refresh token successfully!");
          apiResponse.setResult(res);

          return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                    .body(apiResponse);
     }

     @PostMapping("/logout")
     public ResponseEntity<ApiResponse<Void>> logout() {
          String emailUsernamePhone = SecurityUtils.getCurrentUserLogin().isPresent()
                    ? SecurityUtils.getCurrentUserLogin().get()
                    : "";

          if (emailUsernamePhone.equals("")) {
               throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
          }

          // update refresh token = null
          this.userService.updateUserToken(null, emailUsernamePhone);

          ApiResponse<Void> apiResponse = new ApiResponse<>();
          apiResponse.setCode(1000);
          apiResponse.setMessage("Log out successfully!");

          // remove fresh token from cookie`
          ResponseCookie deleteSpringCookie = ResponseCookie
                    .from("refresh_token", null)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0)
                    .build();

          return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                    .body(apiResponse);

     }

     @PostMapping("/register")
     public ApiResponse<UserResponse> register(@Valid @RequestBody UserCreationRequest reqUser) {

          User resUser = this.userService.createUser(reqUser);

          return ApiResponse.<UserResponse>builder()
                    .code(1000)
                    .message("register successfully!")
                    .result(userMapper.toUserResponse(resUser))
                    .build();
     }
}

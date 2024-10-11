package com.me_social.MeSocial.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me_social.MeSocial.entity.dto.response.ApiResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

     private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

     private final ObjectMapper mapper;

     public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
          this.mapper = mapper;
     }

     @Override
     public void commence(HttpServletRequest request, HttpServletResponse response,
               AuthenticationException authException) throws IOException, ServletException {
          this.delegate.commence(request, response, authException);
          response.setContentType("application/json;charset=UTF-8");

          ApiResponse<Object> res = new ApiResponse<>();
          res.setCode(HttpStatus.UNAUTHORIZED.value());

          String errorMessage = Optional.ofNullable(authException.getCause()) // NULL
                    .map(Throwable::getMessage)
                    .orElse(authException.getMessage());

          res.setMessage(
                    "Token không hợp lệ (hết hạn, không đúng định dạng, hoặc không truyền JWT ở header)..."
                              + " Specific error: " + errorMessage);

          mapper.writeValue(response.getWriter(), res);
     }
}

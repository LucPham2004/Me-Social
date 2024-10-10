package com.me_social.MeSocial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),

    ENTITY_EXISTED(1002, "Entity existed!", HttpStatus.BAD_REQUEST),

    USERNAME_INVALID(1003, "Username must be at least 4 characters", HttpStatus.UNAUTHORIZED),

    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.UNAUTHORIZED),

    ENTITY_NOT_EXISTED(1005, "Entity not existed", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1006, "User not authenticated", HttpStatus.UNAUTHORIZED), 

    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),

    INVALID_ACTION(1008, "Invalid action that cannot be done", HttpStatus.EXPECTATION_FAILED),

    INVALID_DOB(1009, "You must be at least 16 years old", HttpStatus.BAD_REQUEST)

    ;

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}

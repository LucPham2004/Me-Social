package com.me_social.MeSocial.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
}

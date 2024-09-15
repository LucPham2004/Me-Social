package com.me_social.MeSocial.entity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
    private int code;
    private String message;
    private T result; 
}

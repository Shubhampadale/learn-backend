package com.elearn.app.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class LoginRequest {

    private String email;

    private String password;

}

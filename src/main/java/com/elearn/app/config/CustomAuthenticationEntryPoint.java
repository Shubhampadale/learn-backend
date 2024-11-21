package com.elearn.app.config;

import com.elearn.app.dto.CustomMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //we have to pass JSON at front end then we have to write the JSON
        //we may can make use of CustomMessage
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Invalid Details !!"+authException.getMessage());
        customMessage.setSuccess(false);

        //Now printwriter only accept the string not an JSON object.
        //so we need to convert this JSON object into the String
        //for this we will use objectMapper
        ObjectMapper objectMapper =  new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(customMessage);

        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonString);

        //authException.printStackTrace();

    }
}

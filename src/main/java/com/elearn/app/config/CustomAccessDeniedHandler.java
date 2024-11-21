package com.elearn.app.config;

import com.elearn.app.dto.CustomMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("Permission Denied!!"+accessDeniedException.getMessage());
        customMessage.setSuccess(false);

        //Now printwriter only accept the string not an JSON object.
        //so we need to convert this JSON object into the String
        //for this we will use objectMapper
        ObjectMapper objectMapper =  new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(customMessage);

        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonString);
    }
}

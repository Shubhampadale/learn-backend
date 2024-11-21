package com.elearn.app.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authorization : Bearer 240u34fbffbjdfjdfn
       String authorizationHeader =  request.getHeader("Authorization");
       String username = null;
       String jwtToken = null;

       if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer")){

           //get token from the authorization
           jwtToken =  authorizationHeader.substring(7);
           username =   jwtUtil.extractUsername(jwtToken);

           //we will check the username is null or not
           //also we will check whether SecurityContext having any authenctication object or not
           //if SecurityContext does not hold any authentication then we have to first validate the user and then authenticate
           if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

               //validate
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtToken!=null && jwtUtil.validateToken(jwtToken,userDetails.getUsername())){

                    //authentication to security
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
           }

       }
       //else{

           //try {
            //   throw new AuthenticationException("Token not present!!");
          // } catch (AuthenticationException e) {
           //    throw new RuntimeException(e);
           //}
       //}
       filterChain.doFilter(request,response);
    }
}

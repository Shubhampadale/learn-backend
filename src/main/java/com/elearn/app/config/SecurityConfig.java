package com.elearn.app.config;

import com.elearn.app.config.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private AuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(AuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    //to configure the user in userdetailsservicebean
    /*
    @Bean
    public UserDetailsService userDetailsService(){

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(User.withDefaultPasswordEncoder().username("abc").password("abc").roles("ADMIN").build());

        userDetailsManager.createUser(User.withDefaultPasswordEncoder().username("shubham").password("shubham").roles("USER").build());


        return userDetailsManager;
    }*/

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.cors(e-> e.disable());
        httpSecurity.csrf(e-> e.disable());

        //here you can write your customisation code
        httpSecurity.authorizeHttpRequests(

                auth-> {

                        auth.requestMatchers("/api/v1/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/**").hasRole("GUEST")
                                .requestMatchers(HttpMethod.POST,"/api/v1/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/**").hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                        ;
                         //    auth.requestMatchers(HttpMethod.GET,"/api/v1/categories").permitAll()
                         //.requestMatchers("/client-login","/client-login-process").permitAll()
                         //.requestMatchers(HttpMethod.GET,"/api/v1/courses","api/v1/users").permitAll()
                         //.requestMatchers("apis/v1/users").permitAll()
                         //.anyRequest()
                         //.authenticated();
                }
        )
                .exceptionHandling(exp->
                {
                   exp.accessDeniedHandler(new CustomAccessDeniedHandler());
                });


        //since the JWT authentication is Stateless so we will not stores the data at backend
        //below will ensures the same
        httpSecurity.sessionManagement(e-> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //Now since we want to add the JWTAuthentication filter before the UsernamePasswordAuthentication filter
        //will write below lines
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        /*  //we have commented below to implement JWT token
        httpSecurity.formLogin(
                form ->{

                    form.loginPage("/client-login");
                    //form.usernameParameter("username");
                    //form.passwordParameter("userPassword");
                    form.loginProcessingUrl("/client-login-process");
                    form.successForwardUrl("/success");
                }
        );

        httpSecurity.logout(logout-> {

            logout.logoutUrl("/logout");
        });
         */
        //it not display you the form basis username n password window
        //it displays/pop - up the JS window for login
        //we have commented below to implement JWT token
        //httpSecurity.httpBasic(Customizer.withDefaults());

        //httpSecurity.httpBasic(exp-> exp.authenticationEntryPoint(authenticationEntryPoint));
        //if you put below authenticationEntryPoint code inside the httpBasic then you will get different
        //authentication exception messages.
        //in below case we are only getting single exception on changing password or username which is not valid

        httpSecurity.exceptionHandling(exp->
        {
            exp.authenticationEntryPoint(authenticationEntryPoint);
        });

        return httpSecurity.build();
    }
}

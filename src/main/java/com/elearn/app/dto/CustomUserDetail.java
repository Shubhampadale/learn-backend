package com.elearn.app.dto;

import com.elearn.app.entities.Role;
import com.elearn.app.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetail implements UserDetails {

    private User user;

    public User getUser() {
        return user;
    }

    public CustomUserDetail(User user) {
        this.user = user;
    }


    //below method will be in use when we have to assign role and authorization
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //create and return user roles/authorities

        Set<Role> roles =   user.getRoles();

        Set<SimpleGrantedAuthority> authorities =  roles.stream().map(role-> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

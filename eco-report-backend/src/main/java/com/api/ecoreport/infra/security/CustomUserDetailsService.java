package com.api.ecoreport.infra.security;

import com.api.ecoreport.model.User;
import com.api.ecoreport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRole());
        System.out.println("ROLE DO USER:" + user.getRole().getRole());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singleton(authority));
    }
}

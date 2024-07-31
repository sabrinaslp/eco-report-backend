package com.api.ecoreport.controller;

import com.api.ecoreport.infra.security.TokenService;
import com.api.ecoreport.model.User;
import com.api.ecoreport.model.dto.AuthenticationDTO;
import com.api.ecoreport.model.dto.LoginResponseDTO;
import com.api.ecoreport.model.dto.RegisterDTO;
import com.api.ecoreport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        User user = this.repository.findByEmail(data.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(data.password(), user.getPassword()))  {
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        Optional<User> user = this.repository.findByEmail(data.email());

        if (user.isEmpty()) {
            User newUser = new User();

            newUser.setPassword(passwordEncoder.encode(data.password()));
            newUser.setEmail(data.email());
            newUser.setName(data.name());
            newUser.setNeighborhood(data.neighborhood());
            newUser.setRole(data.role());

            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new LoginResponseDTO(newUser.getName(), token));
        }

        return ResponseEntity.badRequest().build();
    }

}
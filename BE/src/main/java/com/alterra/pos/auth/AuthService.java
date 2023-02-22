package com.alterra.pos.auth;

import com.alterra.pos.config.JwtService;
import com.alterra.pos.dto.ResponseDto;
import com.alterra.pos.entity.User;
import com.alterra.pos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseDto register(RegisterRequest request) {
        try {
            var user = User.builder()
                    .name(request.getName())
                    .position(request.getPosition())
                    .phone(request.getPhone())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .isValid(true)
                    .build();

            repository.save(user);
            return ResponseDto.builder().success(true).message("Success register account").build();
        } catch (Exception e) {
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }

    public ResponseDto login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = repository.findByUsername(request.getUsername()).orElse(null);
            if (user == null) return ResponseDto.builder().success(false).message("Username not found").build();
            var jwtToken = jwtService.generateToken(user);

            return ResponseDto.builder().success(true).message("Log in success").data(
                    JwtResponse.builder().token(jwtToken).id(user.getId())
                            .name(user.getName()).username(user.getUsername())
                            .role(user.getRole()).build()
            ).build();
        } catch (Exception e) {
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }
}

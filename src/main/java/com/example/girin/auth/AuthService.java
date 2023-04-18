package com.example.girin.auth;

import com.example.girin.config.JWTService;
import com.example.girin.user.Role;
import com.example.girin.user.User;
import com.example.girin.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest req){
        //final Optional<User> user = userRepo.findByEmail(req.getEmail());

        User newUser = new User();
        newUser.setEmail(req.getEmail());
        newUser.setFirstname(req.getFirstname());
        newUser.setLastname(req.getLastname());
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setRole(Role.USER);

        userRepo.save(newUser);
        String token = jwtService.generateToken(newUser);

        return AuthenticationResponse.builder().token(token).build();
    }
    public AuthenticationResponse authenticate(AuthRequest req){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );
        var user = userRepo.findByEmail(req.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(token).build();
    }

}

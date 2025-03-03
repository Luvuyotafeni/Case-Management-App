package com.example.CaseManagement.controller;


import com.example.CaseManagement.Dto.LoginRequest;
import com.example.CaseManagement.Dto.LoginResponse;
import com.example.CaseManagement.entity.UserBaseEntity;
import com.example.CaseManagement.service.UserService;
import com.example.CaseManagement.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())

            );

            UserBaseEntity user = userService.getUserByEmail(request.getEmail());

            String token = jwtTokenUtil.generateToken(
                    user.getEmail(),
                    user.getUserId(),
                    user.getRole().toString());

            LoginResponse response = new LoginResponse(user, token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e){
            throw new RuntimeException("Invalid email or password");
        }
    }
}

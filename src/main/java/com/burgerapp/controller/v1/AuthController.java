package com.burgerapp.controller.v1;

import com.burgerapp.domain.Role;
import com.burgerapp.domain.RoleName;
import com.burgerapp.domain.User;
import com.burgerapp.exception.AppException;
import com.burgerapp.repositories.RoleRepository;
import com.burgerapp.repositories.UserRepository;
import com.burgerapp.security.JwtTokenProvider;
import com.burgerapp.security.UserPrincipal;
import com.burgerapp.security.payload.ApiResponse;
import com.burgerapp.security.payload.JwtAuthenticationResponse;
import com.burgerapp.security.payload.LoginRequest;
import com.burgerapp.security.payload.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        //authenticate using username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        String jwt = jwtTokenProvider.generateToken(userId);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,userId));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody SignUpRequest signUpRequest){
        // Following conditions have to be checked.
        // 1) Username already exists. 2)Email Address in use

        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return new ResponseEntity(new ApiResponse(false, "Username already exists"),HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity(new ApiResponse(false, "Email address already exists for another user!!"),HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new AppException("User role not set"));

        user.setRoles(Collections.singleton(userRole));

        User newUser = userRepository.save(user);

        System.out.println("Created USer:" + user.toString());

        String jwt = jwtTokenProvider.generateToken(newUser.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(newUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new JwtAuthenticationResponse(jwt,newUser.getId()));

    }
}

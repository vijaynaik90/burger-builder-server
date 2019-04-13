package com.burgerapp.controller.v1;

import com.burgerapp.domain.User;
import com.burgerapp.exception.ResourceNotFoundException;
import com.burgerapp.mapper.UserMapper;
import com.burgerapp.model.UserDTO;
import com.burgerapp.repositories.UserRepository;
import com.burgerapp.security.CurrentUser;
import com.burgerapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserDTO user = new UserDTO(currentUser.getId(),currentUser.getUsername(),currentUser.getName(),currentUser.getEmail());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public  ResponseEntity<UserDTO> getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User cannot be found with username:"+ username));


        UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(user);

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
}

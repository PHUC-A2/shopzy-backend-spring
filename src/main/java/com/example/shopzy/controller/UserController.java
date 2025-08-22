package com.example.shopzy.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopzy.domain.User;
import com.example.shopzy.domain.request.ReqCreateUserDTO;
import com.example.shopzy.domain.request.ReqUpdateUserDTO;
import com.example.shopzy.domain.response.ResCreateUserDTO;
import com.example.shopzy.domain.response.ResUpdateUserDTO;
import com.example.shopzy.domain.response.ResUserDTO;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.service.UserService;
import com.example.shopzy.util.annotation.ApiMessage;
import com.example.shopzy.util.error.EmailInvalidException;
import com.example.shopzy.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("create a user")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody ReqCreateUserDTO dto)
            throws EmailInvalidException {

        User user = this.userService.convertToReqCreateUserDTO(dto);

        boolean isEmailExists = this.userService.isEmailExists(user.getEmail());
        if (isEmailExists) {
            throw new EmailInvalidException("Email: " + user.getEmail() + " đã tồn tại");
        }

        // hardpasswd
        String hardPassword = this.passwordEncoder.encode(dto.getPassword());
        user.setPassword(hardPassword);


        User userCreate = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(userCreate));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException {

        User user = this.userService.getUserById(id);
        if (user == null) {
            throw new IdInvalidException("User với ID = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(this.userService.convertToResUserDTO(user));
    }

    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@Valid @RequestBody ReqUpdateUserDTO dto)
            throws IdInvalidException {

        User user = this.userService.convertToReqUpdateUserDTO(dto);

        User userUpdate = this.userService.updateUser(user);
        if (userUpdate == null) {
            throw new IdInvalidException("User với ID = " + user.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(userUpdate));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {

        User user = this.userService.getUserById(id);
        if (user == null) {
            throw new IdInvalidException("User với ID = " + id + " không tồn tại");
        }
        this.userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }
}

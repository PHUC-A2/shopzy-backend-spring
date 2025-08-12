package com.example.shopzy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shopzy.domain.User;
import com.example.shopzy.domain.request.ReqCreateUserDTO;
import com.example.shopzy.domain.request.ReqUpdateUserDTO;
import com.example.shopzy.domain.response.ResCreateUserDTO;
import com.example.shopzy.domain.response.ResUpdateUserDTO;
import com.example.shopzy.repository.UserRepository;

@Service
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CRUD
    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public User updateUser(User userReq) {
        User user = this.getUserById(userReq.getId());
        if (user != null) {
            user.setName(userReq.getName());
            user.setFullName(userReq.getFullName());
            user.setPhoneNumber(userReq.getPhoneNumber());
            user.setStatus(userReq.getStatus());
            user.setUpdatedAt(userReq.getUpdatedAt());

            user = this.userRepository.save(user);
        }
        return user;
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    // check email
    public boolean isEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    // convert req create
    public User convertToReqCreateUserDTO(ReqCreateUserDTO userReq) {

        User user = new User();

        user.setName(userReq.getName());
        user.setFullName(userReq.getFullName());
        user.setPassword(userReq.getPassword());
        user.setEmail(userReq.getEmail());
        user.setPhoneNumber(userReq.getPhoneNumber());
        return user;
    }

    // convert res create
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {

        ResCreateUserDTO res = new ResCreateUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setFullName(user.getFullName());
        res.setPassword(user.getPassword());
        res.setEmail(user.getEmail());
        res.setPhoneNumber(user.getPhoneNumber());
        res.setStatus(user.getStatus());
        res.setCreatedAt(user.getCreatedAt());

        return res;
    }


    // convert req update
    public User convertToReqUpdateUserDTO(ReqUpdateUserDTO userDTO) {

        User user = new User();

        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        return user;
    }

    // convert res update
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {

        ResUpdateUserDTO res = new ResUpdateUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setFullName(user.getFullName());
        res.setPhoneNumber(user.getPhoneNumber());
        res.setStatus(user.getStatus());
        res.setUpdatedAt(user.getUpdatedAt());

        return res;
    }

}

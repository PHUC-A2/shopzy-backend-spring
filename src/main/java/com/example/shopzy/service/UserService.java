package com.example.shopzy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shopzy.domain.User;
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

    public User updateUser(Long id, User userReq) {
        User user = this.getUserById(id);
        if (user != null) {
            user.setName(userReq.getName());
            user.setFullName(userReq.getFullName());
            user.setPhoneNumber(userReq.getPhoneNumber());
            // user.setStatus(userReq.getStatus());
            user.setUpdatedAt(userReq.getUpdatedAt());

            user = this.userRepository.save(user);
        }
        return user;
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

}

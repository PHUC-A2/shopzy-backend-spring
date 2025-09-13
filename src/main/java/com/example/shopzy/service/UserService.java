package com.example.shopzy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.shopzy.domain.entity.User;
import com.example.shopzy.domain.request.user.ReqCreateUserDTO;
import com.example.shopzy.domain.request.user.ReqUpdateUserDTO;
import com.example.shopzy.domain.response.ResultPaginationDTO;
import com.example.shopzy.domain.response.user.ResCreateUserDTO;
import com.example.shopzy.domain.response.user.ResUpdateUserDTO;
import com.example.shopzy.domain.response.user.ResUserDTO;
import com.example.shopzy.repository.UserRepository;
import com.example.shopzy.util.error.IdInvalidException;

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

    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {

        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // convert -> user
        List<User> users = pageUser.getContent();
        List<ResUserDTO> resUserDTOs = new ArrayList<>();
        for (User user : users) {
            resUserDTOs.add(convertToResUserDTO(user));
        }
        rs.setResult(resUserDTOs);

        // List<ResUserDTO> listUser = pageUser.getContent()
        // .stream().map(item -> this.convertToResUserDTO(item))
        // .collect(Collectors.toList());
        // rs.setResult(listUser);
        return rs;
    }

    public User getUserById(Long id) throws IdInvalidException {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new IdInvalidException("Không tìm thấy User với ID = " + id);
        }
    }

    public User updateUser(User userReq) throws IdInvalidException {
        User user = this.getUserById(userReq.getId());
        user.setName(userReq.getName());
        user.setFullName(userReq.getFullName());
        user.setPhoneNumber(userReq.getPhoneNumber());
        user.setStatus(userReq.getStatus());
        user.setUpdatedAt(userReq.getUpdatedAt());
        return this.userRepository.save(user);
    }

    public void deleteUser(Long id) throws IdInvalidException {
        User user = this.getUserById(id);
        this.userRepository.deleteById(user.getId());
    }

    // check email
    public boolean isEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    // lấy email để truyền sang UserDetailCustom.java

    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
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
        user.setStatus(userDTO.getStatus());
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

    // convert res get user/users
    public ResUserDTO convertToResUserDTO(User user) {

        ResUserDTO res = new ResUserDTO();

        res.setId(user.getId());
        res.setName(user.getName());
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setPhoneNumber(user.getPhoneNumber());
        res.setStatus(user.getStatus());
        res.setCreatedAt(user.getCreatedAt());
        res.setCreatedBy(user.getCreatedBy());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setUpdatedBy(user.getUpdatedBy());
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}

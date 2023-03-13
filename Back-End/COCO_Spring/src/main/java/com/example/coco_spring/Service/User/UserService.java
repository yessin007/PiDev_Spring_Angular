package com.example.coco_spring.Service.User;

import com.example.coco_spring.Entity.*;
import com.example.coco_spring.Repository.*;
import com.example.coco_spring.Service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User updateRoleUser(Long id, String r) {
        User user = userRepository.findById(id).get();
        user.setRoles(Role.valueOf(r));
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }/*
<<<<<<< HEAD
=======

    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationProvider authType = AuthenticationProvider.valueOf(oauth2ClientName.toUpperCase());
        userRepository.updateAuthenticationType(username, authType);
    }
>>>>>>> parent of 8919370 (errrrr)*/

    public User setUserExpiration (Long id,Integer duration){
        User user = userRepository.findById(id).get();
        user.setExpired(true);
        LocalDate currentDate = LocalDate.now();
        LocalDate unexpirDate = currentDate.plusDays(duration);
        Date dateToUnexpire = Date.from(unexpirDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        user.setDateToUnexired(dateToUnexpire);
        return userRepository.save(user);
    }
}

package com.shafitech.ppmtool.services;

import com.shafitech.ppmtool.domain.User;
import com.shafitech.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.shafitech.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try {

            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            //username has to be unique in not throw exception
            newUser.setUsername(newUser.getUsername());
            //make sure that password and confirm password
            //we dont persist and show password
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);

        }
        catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }
 }
}

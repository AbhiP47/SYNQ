package com.synq.service;

import com.synq.enums.Provider;
import com.synq.helpers.AppConstants;
import com.synq.helpers.ResourceNotFoundException;
import com.synq.entity.User;
import com.synq.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setProvider(Provider.SELF);
        logger.info(user.getProvider().toString());
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User userOld = userRepo.findById(user.getId()).orElseThrow( ()-> new ResourceNotFoundException("User not found"));
        userOld.setName(user.getName());
        userOld.setEmail(user.getEmail());
        if(user.getPassword()!=null && !user.getPassword().isEmpty())
        {
            userOld.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userOld.setAbout(user.getAbout());
        userOld.setPhoneNumber(user.getPhoneNumber());
        userOld.setProfilePic(user.getProfilePic());
        userOld.setEnabled(user.isEnabled());
        userOld.setEmailVerified(user.isEmailVerified());
        userOld.setPhoneVerified(user.isPhoneVerified());
        userOld.setProvider(user.getProvider());
        User saved = userRepo.save(userOld);
        return Optional.ofNullable(saved);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        userRepo.delete(user);
    }

    @Override
    public boolean isUserExist(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null ? true:false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent())
            return optionalUser.get();
        else
            return null;
    }
}

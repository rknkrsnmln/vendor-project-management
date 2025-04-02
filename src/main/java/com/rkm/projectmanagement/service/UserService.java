package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.MyUserPrincipal;
import com.rkm.projectmanagement.entities.User;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //this loadUserByUsername will be called by one of many authentication provider
    //in this case we are using DaoAuthenticationProvider
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        System.out.println(byUsername);
        if(byUsername.isEmpty()) {
            throw new UsernameNotFoundException("username " + username + " is not found.");
        }
//                 .map(user -> new MyUserPrincipal(user))
//                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " is not found."));
        return new MyUserPrincipal(byUsername.get());
    }


    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findById(Integer userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public User save(User newUser) {
        // We NEED to encode plain password before saving to the DB! TODO
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    /**
     * We are not using this update to change user password.
     *
     * @param userId
     * @param update
     * @return
     */
    public User update(Integer userId, User update) {
        User usersUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        usersUser.setUsername(update.getUsername());
        usersUser.setEnabled(update.isEnabled());
        usersUser.setRoles(update.getRoles());
//        line below is not needed, because validation is being done in UserDto
//        usersUser.setRoles(update.getRoles() == null ? usersUser.getRoles() : update.getRoles());
        return this.userRepository.save(usersUser);
    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }
}

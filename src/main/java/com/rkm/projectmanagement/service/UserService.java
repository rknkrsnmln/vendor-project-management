package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.User;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findById(Integer userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public User save(User newHogwartsUser) {
        // We NEED to encode plain password before saving to the DB! TODO
        return this.userRepository.save(newHogwartsUser);
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

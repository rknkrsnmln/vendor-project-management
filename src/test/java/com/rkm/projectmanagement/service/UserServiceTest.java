package com.rkm.projectmanagement.service;

import com.rkm.projectmanagement.entities.User;
import com.rkm.projectmanagement.exception.ObjectNotFoundException;
import com.rkm.projectmanagement.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<User> listUsers;

    @BeforeEach
    void setUp() {
        User u1 = new User();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        User u3 = new User();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        this.listUsers = new ArrayList<>();
        this.listUsers.add(u1);
        this.listUsers.add(u2);
        this.listUsers.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userRepository.
        BDDMockito.given(this.userRepository.findAll()).willReturn(this.listUsers);

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        List<User> actualUsers = this.userService.findAll();

        // Then. Assert expected outcomes.
        Assertions.assertThat(actualUsers.size()).isEqualTo(this.listUsers.size());

        // Verify userRepository.findAll() is called exactly once.
        Mockito.verify(this.userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object userRepository.
        User u = new User();
        u.setId(1);
        u.setUsername("john");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.of(u)); // Define the behavior of the mock object.

        // When. Act on the target behavior. Act steps should cover the method to be tested.
        User returnedUser = this.userService.findById(1);

        // Then. Assert expected outcomes.
        Assertions.assertThat(returnedUser.getId()).isEqualTo(u.getId());
        Assertions.assertThat(returnedUser.getUsername()).isEqualTo(u.getUsername());
        Assertions.assertThat(returnedUser.getPassword()).isEqualTo(u.getPassword());
        Assertions.assertThat(returnedUser.isEnabled()).isEqualTo(u.isEnabled());
        Assertions.assertThat(returnedUser.getRoles()).isEqualTo(u.getRoles());
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void  testFindByIdNotFound() {
        // Given
        BDDMockito.given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = Assertions.catchThrowable(() -> {
            User returnedUser = this.userService.findById(1);
        });

        // Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id of 1");
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
    }

    @Test
    void testSaveSuccess() {
        // Given
        User newUser = new User();
        newUser.setUsername("lily");
        newUser.setPassword("123456");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        BDDMockito.given(this.passwordEncoder.encode(newUser.getPassword())).willReturn("new encoded password");
        BDDMockito.given(this.userRepository.save(newUser)).willReturn(newUser);

        // When
        User returnedUser = this.userService.save(newUser);

        // Then
        Assertions.assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
        Assertions.assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        Assertions.assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
        Assertions.assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());
        Mockito.verify(this.userRepository, Mockito.times(1)).save(newUser);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setUsername("john");
        oldUser.setPassword("123456");
        oldUser.setEnabled(true);
        oldUser.setRoles("admin user");

        User update = new User();
        update.setUsername("john - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.of(oldUser));
        BDDMockito.given(this.userRepository.save(oldUser)).willReturn(oldUser);

        // When
        User updatedUser = this.userService.update(1, update);

        // Then
        Assertions.assertThat(updatedUser.getId()).isEqualTo(1);
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
        Mockito.verify(this.userRepository, Mockito.times(1)).save(oldUser);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        User update = new User();
        update.setUsername("john - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.update(1, update);
        });

        // Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id of 1");
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        User user = new User();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user");

        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.of(user));
        Mockito.doNothing().when(this.userRepository).deleteById(1);

        // When
        this.userService.delete(1);

        // Then
        Mockito.verify(this.userRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        BDDMockito.given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete(1);
        });

        // Then
        Assertions.assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id of 1");
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(1);
    }
}
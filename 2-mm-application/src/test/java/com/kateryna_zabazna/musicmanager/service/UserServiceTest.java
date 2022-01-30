package com.kateryna_zabazna.musicmanager.service;

import com.kateryna_zabazna.musicmanager.application.exception.UserAlreadyExistsException;
import com.kateryna_zabazna.musicmanager.application.exception.UserNotFoundException;
import com.kateryna_zabazna.musicmanager.application.exception.WrongPasswordException;
import com.kateryna_zabazna.musicmanager.application.service.UserService;
import com.kateryna_zabazna.musicmanager.application.service.impl.UserServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.user.User;
import com.kateryna_zabazna.musicmanager.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.kateryna_zabazna.musicmanager.domain.AbstractRepository.AUTO_INC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void prepareTests() {
        MockitoAnnotations.openMocks(this);

        // UserRepository findUserByUsername
        User mockUser0 = new User(0, "kateryna", "12345");
        User mockUser1 = new User(1, "testuser", "testpw");
        User mockUser2 = new User(2, "dhbw", "dhbwpass");
        when(userRepository.findUserByUsername("kateryna")).thenReturn(Optional.of(mockUser0));
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(mockUser1));
        when(userRepository.findUserByUsername("dhbw")).thenReturn(Optional.of(mockUser2));

        // UserRepository findAllUsers
        List<User> mockUsers = List.of(
                mockUser0,
                mockUser1,
                mockUser2
        );
        when(userRepository.findAllUsers()).thenReturn(mockUsers);

        // UserRepository save()
        doNothing().when(userRepository).save(any());
    }

    @Test
    void loginSuccess() {
        // Test data
        UserService userService = new UserServiceImpl(userRepository);

        // Execute
        User actualResult = userService.login("kateryna", "12345");

        // Assert
        assertEquals(0, actualResult.getId());
    }

    @Test
    void loginUnknownUsername() {
        // Test data
        UserService userService = new UserServiceImpl(userRepository);

        // Execute
        String actualErrorMessage = assertThrowsExactly(UserNotFoundException.class, () -> {
            userService.login("typo", "12345");
        }).getMessage();

        // Assert
        assertEquals("The user typo was not found.", actualErrorMessage);
    }

    @Test
    void loginWrongPassword() {
        // Test data
        UserService userService = new UserServiceImpl(userRepository);

        // Execute
        String actualErrorMessage = assertThrowsExactly(WrongPasswordException.class, () -> {
            userService.login("dhbw", "dhbwpss");
        }).getMessage();

        // Assert
        assertEquals("The user dhbw was found, but you entered a wrong password.", actualErrorMessage);
    }

    @Test
    void registerSuccess() {
        // Test data
        UserService userService = new UserServiceImpl(userRepository);

        // Execute
        User actualUser = userService.register("new-user", "new-password");

        // Assert
        User expectedUser = new User(AUTO_INC, "new-user", "new-password");
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void registerUserAlreadyExists() {
        // Test data
        UserService userService = new UserServiceImpl(userRepository);

        // Execute
        String actualErrorMessage = assertThrowsExactly(UserAlreadyExistsException.class, () -> {
            userService.register("kateryna", "");
        }).getMessage();

        // Assert
        assertEquals("The user kateryna already exists.", actualErrorMessage);
    }
}

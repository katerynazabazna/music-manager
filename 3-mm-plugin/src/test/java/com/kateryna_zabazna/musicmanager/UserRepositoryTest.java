package com.kateryna_zabazna.musicmanager;

import com.kateryna_zabazna.musicmanager.domain.user.User;
import com.kateryna_zabazna.musicmanager.domain.user.UserRepository;
import com.kateryna_zabazna.musicmanager.plugin.repository.UserRepositoryImpl;
import com.kateryna_zabazna.musicmanager.utils.CSVHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.kateryna_zabazna.musicmanager.domain.AbstractRepository.AUTO_INC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private CSVHelper csvHelper;

    @BeforeEach
    void prepareTest() {
        MockitoAnnotations.openMocks(this);

        // CSVHelper read()
        List<String[]> mockedReadResult = List.of(
                new String[]{"0", "kateryna", "12345"},
                new String[]{"1", "johndoe", "generic-password"},
                new String[]{"2", "bjarne", "C++ ftw"}
        );
        when(csvHelper.read()).thenReturn(Optional.of(mockedReadResult));

        // CSVHelper write()
        doNothing().when(csvHelper).write(any(String[].class), any());
    }

    @Test
    void findUserById() {
        // Test data
        UserRepository userRepository = new UserRepositoryImpl(csvHelper);

        // Execute
        Optional<User> optionalUser = userRepository.findUserByUsername("bjarne");

        // Verify
        verify(csvHelper, times(1)).read();

        // Assert
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals(2, user.getId());
    }

    @Test
    void findAllUsers() {
        // Test data
        UserRepository userRepository = new UserRepositoryImpl(csvHelper);

        // Execute
        List<User> users = userRepository.findAllUsers();

        // Verify
        verify(csvHelper, times(1)).read();

        // Assert
        assertEquals(3, users.size());
        assertEquals("johndoe", users.get(1).getUsername());
    }

    @Test
    void save() {
        // Test data
        UserRepository userRepository = new UserRepositoryImpl(csvHelper);
        User newUser = new User(AUTO_INC, "max", "musterfrau");

        // Execute
        userRepository.save(newUser);

        // Verify
        verify(csvHelper, times(2)).read();
        verify(csvHelper, times(1)).write(any(), any());

        // Assert
        assertEquals(4, userRepository.findAllUsers().size());
    }
}

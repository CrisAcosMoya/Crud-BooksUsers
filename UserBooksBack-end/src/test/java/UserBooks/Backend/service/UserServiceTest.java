package UserBooks.Backend.service;

import UserBooks.Backend.domain.model.User;
import UserBooks.Backend.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Juan Perez", "juan.perez@example.com");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.createUser(new User(null, "Juan Perez", "juan.perez@example.com"));
        assertNotNull(result);
        assertEquals("Juan Perez", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user, new User(2L, "Ana Gomez", "ana.gomez@example.com"));
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByIdFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("Juan Perez", result.get().getName());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserById(1L);
        assertFalse(result.isPresent());
    }
}
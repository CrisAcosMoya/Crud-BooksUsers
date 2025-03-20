package UserBooks.Backend.infrastructure.apirest;

import UserBooks.Backend.domain.model.User;
import UserBooks.Backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User(1L, "Juan Perez", "juan.perez@example.com");
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Juan Perez")));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user2 = new User(2L, "Ana Gomez", "ana.gomez@example.com");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserByIdFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Juan Perez")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1L);
    }
}
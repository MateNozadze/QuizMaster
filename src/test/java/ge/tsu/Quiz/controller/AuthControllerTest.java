package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.model.User;
import ge.tsu.Quiz.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return Mockito.mock(PasswordEncoder.class);
        }
    }

    @Test
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234", "encodedPassword")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("username", "john")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testLoginFailure() throws Exception {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .param("username", "john")
                        .param("password", "wrong")
                        .with(csrf())) // ← აქ!
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testSignupSuccess() throws Exception {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        mockMvc.perform(post("/signup")
                        .param("username", "newuser")
                        .param("password", "pass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}

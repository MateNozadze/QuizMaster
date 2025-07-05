package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.model.User;
import ge.tsu.Quiz.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
@Import(ProfileControllerTest.MockConfig.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Test
    @WithMockUser(username = "john")
    void testProfilePageSuccess() throws Exception {


        User user = new User();
        user.setUsername("john");
        user.setQuizzes(Collections.emptyList());

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("quizzes"));
    }

}

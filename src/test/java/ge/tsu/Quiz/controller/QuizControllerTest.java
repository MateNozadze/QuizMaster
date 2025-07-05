package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.config4profiles.QuizConfig;
import ge.tsu.Quiz.service.QuizService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
@Import(QuizControllerTest.QuizTestConfig.class)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizConfig quizConfig;

    @TestConfiguration
    static class QuizTestConfig {
        @Bean
        public QuizService quizService() {
            return Mockito.mock(QuizService.class);
        }

        @Bean
        public QuizConfig quizConfig() {
            return Mockito.mock(QuizConfig.class);
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldReturnQuizzesPage() throws Exception {
        when(quizService.getAllQuizzes()).thenReturn(Collections.emptyList());
        when(quizConfig.getAppName()).thenReturn("TestApp");
        when(quizConfig.isEnableDescription()).thenReturn(true);

        mockMvc.perform(get("/quizzes"))
                .andExpect(status().isOk())
                .andExpect(view().name("quizzes"))
                .andExpect(model().attributeExists("quizzes"))
                .andExpect(model().attribute("appName", "TestApp"))
                .andExpect(model().attribute("enableDescription", true));
    }
}

package ge.tsu.Quiz.controller;

import ge.tsu.Quiz.config4profiles.QuizConfig;
import ge.tsu.Quiz.service.QuizService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizSetupController.class)
@Import(QuizSetupControllerTest.TestMockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizSetupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizConfig quizConfig;

    @TestConfiguration
    static class TestMockConfig {

        @Bean
        public QuizService quizService() {
            return Mockito.mock(QuizService.class);
        }

        @Bean
        public QuizConfig quizConfig() {
            QuizConfig config = Mockito.mock(QuizConfig.class);
            when(config.getMaxQuestions()).thenReturn(10);
            when(config.isEnableDescription()).thenReturn(true);
            return config;
        }
    }



    @Test
    void testInvalidQuestionCounts() throws Exception {
        mockMvc.perform(post("/setup")
                        .param("quizName", "Quiz")
                        .param("questionCount", "20"))
                .andExpect(redirectedUrl("/setup"))
                .andExpect(flash().attributeExists("error"));

        mockMvc.perform(post("/setup")
                        .param("quizName", "Quiz")
                        .param("questionCount", "0"))
                .andExpect(redirectedUrl("/setup"))
                .andExpect(flash().attributeExists("error"));
    }
}

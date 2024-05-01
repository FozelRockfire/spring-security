package t1.study.springsecurity.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import t1.study.springsecurity.configuration.TestConfig;
import t1.study.springsecurity.dto.JwtRefreshRequest;
import t1.study.springsecurity.dto.JwtResponse;
import t1.study.springsecurity.dto.LogInRequest;
import t1.study.springsecurity.dto.UserDTO;
import t1.study.springsecurity.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@Import(TestConfig.class)
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserDTO userDTO;
    LogInRequest logInRequest;
    JwtRefreshRequest jwtRefreshRequest;
    JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .name("User")
                .username("User@mail.com")
                .password("password")
                .build();
        logInRequest = LogInRequest.builder()
                .username("User@mail.com")
                .password("password")
                .build();
        jwtRefreshRequest = JwtRefreshRequest.builder()
                .refreshToken("refreshToken")
                .build();
        jwtResponse = JwtResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    void signUp_Success() throws Exception {
        //Arrange
        when(authService.signUp(any(UserDTO.class))).thenReturn(jwtResponse);

        //Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(jwtResponse.accessToken()))
                .andExpect(jsonPath("$.refreshToken").value(jwtResponse.refreshToken()));

        verify(authService).signUp(any(UserDTO.class));
    }

    @Test
    @DisplayName("Вход существующего пользователя")
    void signIn_Success() throws Exception {
        //Arrange
        when(authService.LogIn(any(LogInRequest.class))).thenReturn(jwtResponse);

        //Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logInRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(jwtResponse.accessToken()))
                .andExpect(jsonPath("$.refreshToken").value(jwtResponse.refreshToken()));

        verify(authService).LogIn(any(LogInRequest.class));
    }

    @Test
    @DisplayName("Обновление токена доступа")
    void refresh_Success() throws Exception {
        //Arrange
        when(authService.refresh(any(JwtRefreshRequest.class))).thenReturn(jwtResponse);

        //Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value(jwtResponse.accessToken()));

        verify(authService).refresh(any(JwtRefreshRequest.class));
    }
}
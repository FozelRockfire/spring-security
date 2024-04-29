package t1.study.springsecurity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import t1.study.springsecurity.dto.JwtRefreshRequest;
import t1.study.springsecurity.dto.JwtResponse;
import t1.study.springsecurity.dto.LogInRequest;
import t1.study.springsecurity.dto.UserDTO;
import t1.study.springsecurity.exception.AlreadyExistException;
import t1.study.springsecurity.exception.NotRegisteredException;
import t1.study.springsecurity.exception.TokenValidationException;
import t1.study.springsecurity.model.RoleType;
import t1.study.springsecurity.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService тесты")
class AuthServiceTest {

    @Mock
    JwtService jwtService;
    @Mock
    UserService userService;
    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthService authService;

    User user;
    UserDTO userDTO;
    LogInRequest logInRequest;
    JwtRefreshRequest jwtRefreshRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("User")
                .username("User@mail.com")
                .password("encoded")
                .roles(new HashSet<>(Collections.singleton(RoleType.ROLE_USER)))
                .build();
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
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void testSignUp_UserDoesNotExist_SuccessfulSignUp() {
        //Arrange
        when(userService.findOptionalByUsername("User@mail.com")).thenReturn(Optional.empty());
        when(userService.createUser(userDTO)).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        //Act
        var result = authService.signUp(userDTO);

        //Assert
        assertNotNull(result);
        assertEquals(result, JwtResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build());

        verify(userService).findOptionalByUsername(userDTO.username());
        verify(userService).createUser(userDTO);
        verify(jwtService).generateToken(any(User.class));
        verify(jwtService).generateRefreshToken(any(User.class));
    }

    @Test
    @DisplayName("Ошибка регистрации: пользователь уже существует")
    void testSignUp_UserExists_ThrowAlreadyExistException() {
        //Arrange
        when(userService.findOptionalByUsername("User@mail.com")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(AlreadyExistException.class, () -> authService.signUp(userDTO));
    }

    @Test
    @DisplayName("Вход пользователя")
    void testLogIn_Success() {
        // Arrange
        when(userService.findOptionalByUsername("User@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        // Act
        var response = authService.LogIn(logInRequest);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());

        verify(userService).findOptionalByUsername(userDTO.username());
        verify(jwtService).generateToken(any(User.class));
        verify(jwtService).generateRefreshToken(any(User.class));
    }

    @Test
    @DisplayName("Ошибка входа: пользователь не найден")
    void testLogIn_UserDoesNotExists_ThrowIllegalArgumentException() {
        //Arrange
        when(userService.findOptionalByUsername("User@mail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.LogIn(logInRequest));
    }

    @Test
    @DisplayName("Успешное обновление токена")
    void refresh_Success() {
        // Arrange
        when(jwtService.extractUser("refreshToken")).thenReturn(user.getUsername());
        when(userService.findOptionalByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(jwtRefreshRequest.refreshToken(), user)).thenReturn(true);
        when(jwtService.isRefreshToken(jwtRefreshRequest.refreshToken())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");

        // Act
        JwtResponse response = authService.refresh(jwtRefreshRequest);

        // Assert
        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());

        verify(jwtService).extractUser("refreshToken");
        verify(userService).findOptionalByUsername(user.getUsername());
        verify(jwtService).isTokenValid(jwtRefreshRequest.refreshToken(), user);
        verify(jwtService).isRefreshToken(jwtRefreshRequest.refreshToken());
        verify(jwtService).generateToken(user);
    }

    @Test
    @DisplayName("Обновление токена - ошибка: пользователь не зарегистрирован")
    void refresh_UserNotRegistered_ThrowsNotRegisteredException() {
        // Arrange
        when(jwtService.extractUser("refreshToken")).thenReturn(null);

        // Act & Assert
        assertThrows(NotRegisteredException.class, () -> authService.refresh(jwtRefreshRequest));
    }

    @Test
    @DisplayName("Обновление токена - ошибка: недействительный refresh токен")
    void refresh_InvalidRefreshToken_ThrowsTokenValidationException() {
        // Arrange
        when(jwtService.extractUser("refreshToken")).thenReturn(user.getUsername());
        when(userService.findOptionalByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("refreshToken", user)).thenReturn(false);

        // Act & Assert
        assertThrows(TokenValidationException.class, () -> authService.refresh(jwtRefreshRequest));
    }
}
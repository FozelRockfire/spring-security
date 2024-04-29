package t1.study.springsecurity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import t1.study.springsecurity.dto.UserDTO;
import t1.study.springsecurity.exception.NotFoundException;
import t1.study.springsecurity.model.RoleType;
import t1.study.springsecurity.model.User;
import t1.study.springsecurity.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService тесты")
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    UserDTO userDTO;
    User user;

    @BeforeEach
    void setUp(){
        userDTO = UserDTO.builder()
                .name("User")
                .username("User@mail.com")
                .password("password")
                .build();

        user = User.builder()
                .id(1L)
                .name("User")
                .username("User@mail.com")
                .password("encoded")
                .roles(new HashSet<>(Collections.singleton(RoleType.ROLE_USER)))
                .build();
    }

    @Test
    @DisplayName("Создание пользователя")
    void testCreateUser() {
        //Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        //Act
        var result = userService.createUser(userDTO);

        //Assert
        assertNotNull(result);
        assertEquals(result, user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Поиск по имени пользователя - пользователь существует")
    void testFindByUsername_UsernameExists_ReturnsUser() {
        //Arrange
        when(userRepository.findByUsername("User@mail.com")).thenReturn(Optional.of(user));

        //Act
        var result = userService.findByUsername("User@mail.com");

        //Assert
        assertNotNull(result);
        assertEquals(result, user);
        verify(userRepository).findByUsername("User@mail.com");
    }

    @Test
    @DisplayName("Поиск по имени пользователя - пользователь не существует")
    void testFindByUsername_UsernameDoesNotExist_ThrowNotFoundException(){
        // Arrange
        when(userRepository.findByUsername("notexist@mail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.findByUsername("notexist@mail.com"));
    }

    @Test
    @DisplayName("Поиск Optional по имени пользователя - пользователь существует")
    void testFindOptionalByUsername_UsernameExists_ReturnsUser() {
        //Arrange
        when(userRepository.findByUsername("User@mail.com")).thenReturn(Optional.of(user));

        //Act
        var result = userService.findOptionalByUsername("User@mail.com");

        //Assert
        assertTrue(result.isPresent());
        assertEquals(result.get(), user);
        verify(userRepository).findByUsername("User@mail.com");
    }

    @Test
    @DisplayName("Поиск Optional по имени пользователя - пользователь не существует")
    void testFindOptionalByUsername_UsernameDoesNotExist_ReturnEmpty() {
        // Arrange
        when(userRepository.findByUsername("User@mail.com")).thenReturn(Optional.empty());

        // Act
        var result = userService.findOptionalByUsername("User@mail.com");

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByUsername("User@mail.com");
    }

    @Test
    @DisplayName("UserDetailsService - пользователь существует")
    void testUserDetailsService_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("User@mail.com")).thenReturn(Optional.of(user));

        // Act
        UserDetailsService detailsService = userService.userDetailsService();
        var userDetails = detailsService.loadUserByUsername("User@mail.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        verify(userRepository).findByUsername("User@mail.com");
    }
}
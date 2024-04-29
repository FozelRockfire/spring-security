package t1.study.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import t1.study.springsecurity.dto.JwtRefreshRequest;
import t1.study.springsecurity.dto.JwtResponse;
import t1.study.springsecurity.dto.LogInRequest;
import t1.study.springsecurity.dto.UserDTO;
import t1.study.springsecurity.exception.AlreadyExistException;
import t1.study.springsecurity.exception.NotFoundException;
import t1.study.springsecurity.exception.NotRegisteredException;
import t1.study.springsecurity.exception.TokenValidationException;
import t1.study.springsecurity.model.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public JwtResponse signUp(UserDTO request) {
        userService.findOptionalByUsername(request.username())
                .ifPresent(user -> {
                    throw new AlreadyExistException("Пользователь с таким username уже существует");
                });

        User user = userService.createUser(request);

        return JwtResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public JwtResponse LogIn(LogInRequest request) {
        User user = userService.findOptionalByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Неверное имя пользователя или пароль"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        return JwtResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public JwtResponse refresh(JwtRefreshRequest request) {
        String username = jwtService.extractUser(request.refreshToken());
        if (username != null) {
            User user = userService.findOptionalByUsername(username)
                    .orElseThrow(() -> new NotFoundException("Пользователь с таким username не найден"));
            if (jwtService.isTokenValid(request.refreshToken(), user) && jwtService.isRefreshToken(request.refreshToken())) {
                String accessToken = jwtService.generateToken(user);
                return JwtResponse.builder()
                        .accessToken(accessToken)
                        .build();
            } else {
                throw new TokenValidationException("Недействительный refresh токен");
            }
        } else {
            throw new NotRegisteredException("Пользователь не зарегистрирован");
        }
    }
}

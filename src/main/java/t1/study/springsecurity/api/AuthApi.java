package t1.study.springsecurity.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import t1.study.springsecurity.dto.JwtRefreshRequest;
import t1.study.springsecurity.dto.JwtResponse;
import t1.study.springsecurity.dto.LogInRequest;
import t1.study.springsecurity.dto.UserDTO;

@Tag(name = "Authentication", description = "API для аутентификации и авторизации")
@RequestMapping("/api/auth")
public interface AuthApi {

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class)))
    @PostMapping("register")
    ResponseEntity<JwtResponse> signUp(@RequestBody UserDTO request);

    @Operation(summary = "Вход существующего пользователя")
    @ApiResponse(responseCode = "200", description = "Успешный вход",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class)))
    @PostMapping("login")
    ResponseEntity<JwtResponse> signIn(@RequestBody LogInRequest request);

    @Operation(summary = "Обновление токена доступа")
    @ApiResponse(responseCode = "200", description = "Токен успешно обновлен",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class)))
    @PostMapping("refresh")
    ResponseEntity<JwtResponse> refresh(@RequestBody JwtRefreshRequest request);
}
package t1.study.springsecurity.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import t1.study.springsecurity.api.AuthApi;
import t1.study.springsecurity.dto.*;
import t1.study.springsecurity.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public ResponseEntity<JwtResponse> signUp(@RequestBody UserDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signUp(request));
    }

    @Override
    public ResponseEntity<JwtResponse> signIn(@RequestBody LogInRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.signIn(request));
    }

    @Override
    public ResponseEntity<JwtResponse> refresh(@RequestBody JwtRefreshRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authService.refresh(request));
    }
}
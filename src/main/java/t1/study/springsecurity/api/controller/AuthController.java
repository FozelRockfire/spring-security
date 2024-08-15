package t1.study.springsecurity.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private HttpHeaders headers;

    @Override
    public ResponseEntity<Void> signUp(@RequestBody UserDTO request) {
        JwtResponse jwtResponse = (authService.signUp(request));

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtResponse.accessToken());
        headers.set("Refresh", "Refresh " + jwtResponse.refreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .build();
    }

    @Override
    public ResponseEntity<Void> signIn(@RequestBody LogInRequest request) {
        JwtResponse jwtResponse = authService.LogIn(request);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtResponse.accessToken());
        headers.set("Refresh", "Refresh " + jwtResponse.refreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .build();
    }

    @Override
    public ResponseEntity<Void> refresh(@RequestBody JwtRefreshRequest request) {
        JwtResponse jwtResponse = authService.refresh(request);

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtResponse.accessToken());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .build();
    }
}
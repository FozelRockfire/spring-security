package t1.study.springsecurity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LogInRequest(

        @Schema(description = "Username пользователя",
                example = "johndoe@example.com")
        String username,

        @Schema(description = "Пароль пользователя",
                example = "password123")
        String password
){
}

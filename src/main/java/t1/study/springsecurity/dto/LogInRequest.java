package t1.study.springsecurity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LogInRequest(

        @Schema(description = "Username пользователя",
                example = "johndoe@gmail.com")
        String username,

        @Schema(description = "Пароль пользователя",
                example = "admin")
        String password
){
}

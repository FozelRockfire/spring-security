package t1.study.springsecurity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserDTO(

        @Schema(description = "Имя пользователя",
                example = "John Doe")
        String name,

        @Schema(description = "Электронная почта пользователя, используемая в качестве логина",
                example = "johndoe@example.com")
        String username,

        @Schema(description = "Пароль пользователя",
                example = "password123")
        String password
){
}

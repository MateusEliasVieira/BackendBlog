package com.seminfo.api.dto.user;

import com.seminfo.utils.Field;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.seminfo.utils.Field.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDTO {

    @NotBlank(message = Field.NAME_MESSAGE)
    @Size(min = 4, message = Field.NAME_SIZE_MESSAGE)
    @Pattern(regexp = "^[A-Z]+(.)*", message = FIRST_LETTER_NAME_MESSAGE) // garante que a primeira letra seja maiuscula
    private String name;

    @NotBlank(message = Field.USERNAME_MESSAGE)
    private String username;

    @NotBlank(message = Field.PASSWORD_MESSAGE)
    @Size(min = 6, message = Field.PASSWORD_SIZE_MESSAGE)
    private String password;

    @NotBlank(message = Field.ABOUT_MESSAGE)
    private String about;

    @NotBlank(message = Field.EMAIL_MESSAGE)
    @Email(message = Field.EMAIL_VALID_MESSAGE)
    private String email;

    @NotBlank(message = Field.PHOTO_MESSAGE)
    private String image;

}

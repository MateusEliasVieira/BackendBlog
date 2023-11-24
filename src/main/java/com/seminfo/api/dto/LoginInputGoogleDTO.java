package com.seminfo.api.dto;

import com.seminfo.utils.Field;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginInputGoogleDTO
{

    @NotBlank(message = Field.NAME_MESSAGE)
    @Size(min = 4, message = Field.NAME_SIZE_MESSAGE)
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

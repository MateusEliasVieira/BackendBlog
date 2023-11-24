package com.seminfo.api.dto;

import com.seminfo.utils.Field;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordInputDTO {

    @NotBlank(message = Field.TOKEN_MESSAGE)
    private String token;

    @NotBlank(message = Field.PASSWORD_MESSAGE)
    @Size(min = 6, message = Field.PASSWORD_SIZE_MESSAGE)
    private String newpassword;

}

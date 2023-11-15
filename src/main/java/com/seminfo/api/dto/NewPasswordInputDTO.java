package com.seminfo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordInputDTO {

    @NotBlank
    private String token;
    @NotBlank
    @Size(min = 6)
    private String newpassword;

}

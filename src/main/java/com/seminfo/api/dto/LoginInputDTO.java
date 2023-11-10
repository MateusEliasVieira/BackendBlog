package com.seminfo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInputDTO {

    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

}

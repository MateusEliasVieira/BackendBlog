package com.seminfo.api.dto;

import com.seminfo.domain.enums.Permissions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginOutputDTO
{

    @NotNull
    private Long idUser;
    @NotBlank
    private String token;
    @NotNull
    private Permissions permission;

}

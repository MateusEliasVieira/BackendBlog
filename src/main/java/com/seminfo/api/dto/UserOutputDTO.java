package com.seminfo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO
{

    @NotNull
    private Long idUser;
    @NotBlank
    @Size(min = 4)
    private String name;
    @NotBlank
    private String about;
    @NotBlank
    private String image;

}

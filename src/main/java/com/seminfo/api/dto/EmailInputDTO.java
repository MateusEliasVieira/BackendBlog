package com.seminfo.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailInputDTO
{

    @NotBlank
    private String subject;
    @NotBlank
    private String message;
    @NotBlank
    private String to;

}

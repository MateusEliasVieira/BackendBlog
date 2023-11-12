package com.seminfo.api.dto;

import com.seminfo.domain.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostInputDTO
{

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Date datePublish = new Date();
    @NotBlank
    private String image;
    @NotNull
    private User user;

}

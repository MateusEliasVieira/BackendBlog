package com.seminfo.api.dto;

import com.seminfo.domain.model.User;
import com.seminfo.utils.Field;
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

    @NotBlank(message = Field.TITLE_MESSAGE)
    private String title;

    @NotBlank(message = Field.CONTENT_MESSAGE)
    private String content;

    @NotNull
    private Date datePublish = new Date();

    @NotBlank(message = Field.IMAGE_POST_MESSAGE)
    private String image;

    @NotNull
    private User user;

}

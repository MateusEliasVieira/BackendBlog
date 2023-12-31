package com.seminfo.api.dto.post;

import com.seminfo.api.dto.user.UserOutputDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOutputDTO {

    @NotNull
    private Long idPost;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private Date datePublish;
    @NotBlank
    private String image;

    @NotNull
    private UserOutputDTO user;

}

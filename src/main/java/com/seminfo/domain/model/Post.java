package com.seminfo.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post implements Serializable
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;
    @NotBlank
    private String title;
    @NotBlank
    @Column(columnDefinition = "text")
    private String content;
    @NotNull
    private Date datePublish;
    @NotBlank
    @Column(columnDefinition = "text")
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

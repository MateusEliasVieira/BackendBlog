package com.seminfo.domain.model;

import com.seminfo.domain.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @NotBlank
    @Size(min = 4)
    //@Pattern(regexp = "^[A-Z]+(.)*") // garante que a primeira letra seja maiuscula
    private String name;
    @NotBlank
    @Column(unique = true)
    private String username;
    @NotBlank
    @Size(min = 6)
    private String password;
    @NotBlank
    @Column(columnDefinition = "text")
    private String about;
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank
    @Column(columnDefinition = "text")
    private String image;
    @Column(columnDefinition = "text")
    private String token;
    @Column(nullable = false)
    private boolean status;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @NotNull
    private int attempts;

    private Date releaseLogin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        if(this.role == Roles.ROLE_ADMIN)
        {
            // se esse usuário tiver uma role de admin, retornamos os tipos de acesso que ele pode ter no sistema, nesse caso admin e user
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }

        if(this.role == Roles.ROLE_USER)
        {
            // se esse usuário tiver uma role de user, retornamos o tipo de acesso que ele pode ter no sistema, nesse caso apenas user
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        else
        {
            return null;
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

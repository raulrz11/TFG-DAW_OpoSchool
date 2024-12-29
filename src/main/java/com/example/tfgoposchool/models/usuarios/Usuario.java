package com.example.tfgoposchool.models.usuarios;

import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.Profesor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Usuario implements UserDetails {
    public static String IMAGE_DEFAULT = "https://cdn-icons-png.flaticon.com/512/5675/5675152.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    @Builder.Default
    private String imagenPerfil = IMAGE_DEFAULT;
    @Column
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Column
    @Builder.Default
    private Boolean isActive = true;
    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Rol> rols;
    @OneToOne(mappedBy = "usuario")
    private Alumno alumno;
    @OneToOne(mappedBy = "usuario")
    private Profesor profesor;

    public enum Rol{
        ADMIN, PROFESOR, ALUMNO
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rols.stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

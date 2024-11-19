package uz.raximov.postcrud.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.raximov.postcrud.enums.Role;


@Entity
@Table(name = "\"User\"")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.ROLE_ADMIN;

    @Column(name = "createDate",columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createDate = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole().name()));
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

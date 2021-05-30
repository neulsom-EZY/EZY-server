package com.server.EZY.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "User")
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class UserEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long userIdx;

    @Column(name = "NickName", length = 10, nullable = false)
    @Size(min = 1, max = 10)
    private String nickname;

    @Column(name = "Password", length = 10, nullable = false)
    @Size(min = 4, max = 10)
    private String password;

    @Column(name = "PhoneNumber", length = 11)
    @Size(min = 11, max = 11)
    private String phoneNumber;

    @Column(name = "Permission", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Permission permission;

    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

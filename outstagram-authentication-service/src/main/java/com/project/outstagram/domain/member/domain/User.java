package com.project.outstagram.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.outstagram.BaseAuditingEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseAuditingEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String email;

    private String nickname;

    @JsonIgnore
    private String password;

    @Column(columnDefinition="bit(1) default 1")
    private boolean enabled;

    @Column(columnDefinition="bit(1) default 1")
    private boolean acceptOptionalBenefitAlerts;

    private String fcmToken;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void initialize(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return Long.toString(id);
    }


    @Override
    public boolean isEnabled() {
        return enabled;
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
}

package com.example.timecheck.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;
    @Column(name = "description")
    private String description;

    @Override
    public String getAuthority() {
        return name;
    }
}

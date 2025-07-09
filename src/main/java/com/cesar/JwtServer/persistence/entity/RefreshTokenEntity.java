package com.cesar.JwtServer.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="refresh_Tokens")
@Getter
@NoArgsConstructor(force = true)
public class RefreshTokenEntity {

    public RefreshTokenEntity(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", columnDefinition = "TEXT", length = 303)
    private final String tokenHash;

    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
}
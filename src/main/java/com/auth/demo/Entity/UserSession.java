package com.auth.demo.Entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_sessions", indexes = {
        @Index(name = "idx_session_user_id", columnList = "user_id"),
        @Index(name = "idx_session_expires_at", columnList = "expiresAt")
})
public class UserSession extends SecureEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false, length = 512)
    private String refreshToken;

    private String ipAddress;
    private String userAgent;
    private String browser;
    private String os;
    private String device;
    private Instant expiresAt;
    private Instant lastUsedAt;
}
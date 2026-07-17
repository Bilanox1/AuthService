package com.auth.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.demo.Entity.User;
import com.auth.demo.Entity.UserSession;

@Repository
public interface UserSessionsRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    void deleteAllByUserPublicId(UUID userPublicId);

    Optional<User> findUserByRefreshToken(String refreshToken);

}

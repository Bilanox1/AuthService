package com.auth.demo.services.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import com.auth.demo.Entity.User;

public interface UserSessionsService {

    Boolean createSession(User user, HttpServletRequest request, String refreshToken);

    Boolean isSessionValid(String refreshToken);

    User getUserByRefreshToken(String refreshToken);

}
package com.auth.demo.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.demo.Entity.User;
import com.auth.demo.Entity.UserSession;
import com.auth.demo.repository.UserSessionsRepository;
import com.auth.demo.services.interfaces.UserSessionsService;

import jakarta.servlet.http.HttpServletRequest;
import ua_parser.Client;
import ua_parser.Parser;

@Service
public class UserSessionsServiceImpl implements UserSessionsService {

    private final UserSessionsRepository userSessionsRepository;
    private static final Parser uaParser = new Parser();

    public UserSessionsServiceImpl(UserSessionsRepository userSessionsRepository) {
        this.userSessionsRepository = userSessionsRepository;
    }

    @Override
    @Transactional
    public Boolean createSession(User user, HttpServletRequest request, String refreshToken) {
        try {
            String ipAddress = getClientIp(request);

            String rawUserAgent = request.getHeader("User-Agent");

            UserSession session = new UserSession();
            session.setUser(user);
            session.setIpAddress(ipAddress);
            session.setRefreshToken(refreshToken);
            session.setUserAgent(rawUserAgent);

            if (rawUserAgent != null) {
                Client client = uaParser.parse(rawUserAgent);
                session.setBrowser(client.userAgent.family + " " + client.userAgent.major);
                session.setOs(client.os.family + " " + client.os.major);
                session.setDevice(client.device.family);
            }

            session.setLastUsedAt(Instant.now());
            // add 10 days to current time for expiration
            session.setExpiresAt(Instant.now().plus(10, ChronoUnit.DAYS));

            userSessionsRepository.save(session);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean isSessionValid(String refreshToken) {
        return userSessionsRepository.findByRefreshToken(refreshToken)
                .map(session -> session.getExpiresAt().isAfter(Instant.now()))
                .orElse(false);
    }

    @Override
    public User getUserByRefreshToken(String refreshToken) {
        return userSessionsRepository.findByRefreshToken(refreshToken)
                .filter(session -> session.getExpiresAt().isAfter(Instant.now()))
                .map(UserSession::getUser)
                .orElse(null);
    }

    private String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
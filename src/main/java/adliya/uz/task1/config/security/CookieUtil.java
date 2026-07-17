package adliya.uz.task1.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final CookieProperties cookieProperties;
    private final JwtProperties jwtProperties;

    public ResponseCookie createAuthCookie(String token) {
        return ResponseCookie.from(cookieProperties.getName(), token)
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .path("/")
                .maxAge(Duration.ofMillis(jwtProperties.getExpiration()))
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        return ResponseCookie.from(cookieProperties.getName(), "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }
}
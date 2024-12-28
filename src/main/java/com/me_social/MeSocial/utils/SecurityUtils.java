package com.me_social.MeSocial.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.me_social.MeSocial.entity.dto.response.LoginResponse;
import com.nimbusds.jose.util.Base64;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityUtils {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;
    private final JwtEncoder jwtEncoder;

    @Value("${me_social.jwt.base64-secret}")
    private String jwtKey;

    @Value("${me_social.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${me_social.jwt.refresh-token-validity-in-seconds}")
    public Long refreshTokenExpiration;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            System.out.println(">>> JWT error: " + e.getMessage());
            throw e;
        }
    }

    public String createAccessToken(String emailUsernamePhone, LoginResponse dto) {
        LoginResponse.UserInsideToken userToken = new LoginResponse.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setUsername(dto.getUser().getUsername());

        Instant now = Instant.now();
        Instant validity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);

        // hardcode permission for testing
        List<String> listAuthority = new ArrayList<>();

        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        // @formatter:off
          JwtClaimsSet claims = JwtClaimsSet.builder()
          .issuedAt(now)
          .expiresAt(validity)
          .subject(emailUsernamePhone)
          .claim("user", userToken).claim("user_id", dto.getUser().getId())
          .claim("permission", listAuthority)
          .build();
          JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
          return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
     }

     public String createRefreshToken(String emailUsernamPhone, LoginResponse dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        LoginResponse.UserInsideToken userToken = new LoginResponse.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setUsername(dto.getUser().getUsername());
        userToken.setLocation(dto.getUser().getLocation());

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(now)
        .expiresAt(validity)
        .subject(emailUsernamPhone)
        .claim("user", userToken)
        .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
   }

        /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
//     public static boolean isAuthenticated() {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
//     }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
//     public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         return (
//             authentication != null && getAuthorities(authentication).anyMatch(authority -> Arrays.asList(authorities).contains(authority))
//         );
//     }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
//     public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
//         return !hasCurrentUserAnyOfAuthorities(authorities);
//     }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
//     public static boolean hasCurrentUserThisAuthority(String authority) {
//         return hasCurrentUserAnyOfAuthorities(authority);
//     }

//     private static Stream<String> getAuthorities(Authentication authentication) {
//         return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
//     }
}

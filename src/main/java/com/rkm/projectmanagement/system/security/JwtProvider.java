package com.rkm.projectmanagement.system.security;

import com.rkm.projectmanagement.entities.MyUserPrincipal;
import com.rkm.projectmanagement.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private JwtEncoder jwtEncoder;

    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        int expiresIn = 60 * 30;

        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .subject(authentication.getName())
                .claim("authorities", authorities)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}

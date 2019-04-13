package com.burgerapp.security.payload;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long userId;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtAuthenticationResponse(String accessToken, Long userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }
}

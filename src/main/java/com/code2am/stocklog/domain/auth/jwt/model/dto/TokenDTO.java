package com.code2am.stocklog.domain.auth.jwt.model.dto;


import com.code2am.stocklog.domain.auth.common.enums.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;

  public String returnRefreshTokenValue(){
      return this.refreshToken;
  }


    /* Builder */
    @Builder
    public TokenDTO(String grantType, String accessToken, Long accessTokenExpiresIn, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
    }
}

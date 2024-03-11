package com.code2am.stocklog.domain.auth.jwt.model.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;

  public String returnRefreshTokenValue(){
      return this.refreshToken;
  }
}

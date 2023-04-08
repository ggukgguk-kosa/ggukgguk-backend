package com.ggukgguk.api.auth.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ggukgguk.api.member.vo.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthTokenPayload {
	
	String authType;
	String accessToken;
	String refreshToken;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date accessTokenExpiresIn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date refreshTokenExpiresIn;
	Member member;
}

package com.ggukgguk.api.auth.service;

import java.util.HashMap;

import com.ggukgguk.api.member.vo.Member;

public interface AuthService {

	public HashMap<String, Object> login(Member member);
	public HashMap<String, Object> regenToken(String refreshToken);
}

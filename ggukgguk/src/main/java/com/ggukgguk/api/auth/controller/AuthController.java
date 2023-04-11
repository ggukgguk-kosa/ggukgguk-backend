package com.ggukgguk.api.auth.controller;

import java.nio.charset.Charset;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.auth.service.AuthService;
import com.ggukgguk.api.auth.vo.AuthTokenPayload;
import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.member.vo.Member;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	private AuthService service;

	@PostMapping(value = "/login")
	public ResponseEntity<BasicResp<?>> loginHandler(@RequestBody Member reqLoginInfo) {
		BasicResp<Object> respBody = null;
		int respCode = 0;

		final AuthTokenPayload payload = service.login(reqLoginInfo);
		
		if (payload == null) {
			log.debug("로그인 실패");
			respBody = new BasicResp<Object>("error", "로그인에 실패하였습니다.", null);
			respCode = HttpServletResponse.SC_BAD_REQUEST;
		} else {
			log.debug("로그인 성공");
			respBody = new BasicResp<Object>("success", null, payload);
			respCode = HttpServletResponse.SC_OK;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json",
				Charset.forName("UTF-8")));
		
		return new ResponseEntity<BasicResp<?>>(respBody, headers, respCode);
	}
	
	@PostMapping(value = "/refresh")
	public ResponseEntity<BasicResp<?>>
		verifyHandler(@RequestBody HashMap<String, String> reqPayload) {
		
		AuthTokenPayload respPayload = service.regenToken(reqPayload.get("refreshToken"));
		
		BasicResp<?> respBody = null;
		int respCode = 0;
		if (respPayload == null) {
			respBody = new BasicResp<Object>("error", "토큰을 재발급하는데 실패했습니다.", null);
			respCode = HttpServletResponse.SC_BAD_REQUEST;
		} else {
			respBody = new BasicResp<Object>("success", "토큰을 재발급하는데 성공했습니다.", respPayload);
			respCode = HttpServletResponse.SC_OK;
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json",
				Charset.forName("UTF-8")));
		
		return new ResponseEntity<BasicResp<?>>(respBody, headers, respCode);
	}
		
}

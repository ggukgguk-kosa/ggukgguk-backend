package com.ggukgguk.api.auth.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.MementoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.member.dao.MemberDao;
import com.ggukgguk.api.member.vo.Member;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.json.JSONObject;

@Service
public class OAuthService {
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	RestTemplate restTemplate;  // REST API를 호출할 수 있는 내장 Srping 내장 클래스.
	
	// 참고) https://2bmw3.tistory.com/15
	
	// 카카오의 권한 토크를 받는 메소드
	public String getKakaoAccessToken(String code) {
		
		String accessToken = "";

	    // restTemplate을 사용하여 API 호출
	    String reqUrl = "https://kauth.kakao.com/oauth/token"; // 해당 URL에 요청하여  Access token을 받는다.
	    URI uri = URI.create(reqUrl);

	    HttpHeaders headers = new HttpHeaders();

	    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
	    parameters.set("grant_type", "authorization_code");
	    parameters.set("client_id", "88ae00c6ba4b777f197c6d3b5c972acd"); // 카카오 REST API 주소
	    parameters.set("redirect_uri", "http://localhost:8080/api/auth/kakao"); // 리다이렉트 주소
	    // 리다이렉트 주소 의미: 로그인에 성공하면 다시 로그인한 사용자가 만든 페이지로 돌아가야 하는데 그 돌아갈 페이지의 주소가 redirect_uri 
	    parameters.set("code", code); // 카카오에서 전달 받은 인가 코드

	    HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(parameters, headers); //카카오에서 전달 받은 파라미터 값을 인자로 전달하여 전달.
	    ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity(uri, restRequest, JSONObject.class);
	    JSONObject responseBody = apiResponse.getBody();

	    accessToken = (String) responseBody.get("access_token");

		return accessToken; // 카카오의 권한 토큰 발급.
	}

	// 카카오 토큰을 통해 사용자 정보 가져오기. // 카카오 공식문서에 있는 이메일과 프로필에 있는 닉네임, 사용자의 생일(월,일) 정보 전달.
	public JsonNode getkakaoUserInfo(String accessToken) {

		String reqUrl = "https://kapi.kakao.com/v2/user/me";
		URI uri = URI.create(reqUrl);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "bearer " + accessToken);
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("property_keys",
				"[\"kakao_account.profile\",\"kakao_account.name\",\"kakao_account.email\",\"kakao_account.birthday\"]");

		HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(parameters, headers);
		return restTemplate.postForEntity(uri, restRequest, JsonNode.class).getBody();
	}

	// 카카오 로그인 및 회원가입
	public Boolean kakaoLogin(String code) {
			// 1. 먼저 카카오 서비스에 로그인 후 사용자 이용동의를 커치면 카카오에서 인가 코드를 전송해주는 방식으로 진행.
			// 2. 인가 코드를 통해 권한 토큰을 반환시킴.
			String token = getKakaoAccessToken(code);
			// 3. 권한 토큰을 가지고 사용자 정보를 가지지는 형태로 나타냄.
			JsonNode userInfo = getkakaoUserInfo(token);
			log.debug(userInfo);
			// (1) 사용자 아이디 값을 가져와서 DB에 있는 지 여부를 확인 [카카오 로그인]
			Boolean memberCheck = kakaoAccess(userInfo);
			if(memberCheck) {return true; }
//			// (2) DB에 사용자 정보가 없으면 새롭게 가입시키면 됨.
			
			KaKaoRegister(userInfo);
			return true;
	}
		
	// 카카오 DB에서 사용자가 정보가 이미 등록되어 있는지 확인.
	public Boolean kakaoAccess(JsonNode userInfo) {

		String id = userInfo.get("id").asText();
		Member member = memberDao.selectMemberById(id);
		if(member == null) return false;
		return true;
	}

	// 카카오 로그인한 정보를 꾹꾹 서비스에 등록.
	public Boolean KaKaoRegister(JsonNode userInfo) {
			Member member = new Member();
			member.setMemberId(userInfo.get("id").asText().toString()); // 카카오에서 임의로 부여한 ID 값.
			member.setMemberSocial("KAKAO"); // 사용한 소셜 정보
			member.setMemberNickname(userInfo.get("kakao_account").get("profile").get("nickname").asText().toString()); //카카오 프로필 이름
			member.setMemberEmail(userInfo.get("kakao_account").get("email").asText().toString()); // 카카오로 연동한 이메일
			
			// sql구문이  not null로 지정되어 있기에... member VO 나마지 값들을 더미 값으로 삽입.
			member.setMemberName("");
			member.setMemberPw("");
			member.setMemberPhone("");
			member.setMemberBirth(new Date(19700101)); // 카카오에 생일을 받아오나, 월 일 정도 만 불러와서. 임의로 우선 설정.
			member.setMemberAuthority("NORMAL"); // 우선 NORMAL 설정함. => 향후  GUEST 
			log.debug(member);
			try {
				memberDao.insertMember(member);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 위 과정이 정상적으로 진행하면 회원가입이 정상적을 실행.

			return true;
	}

	// 1. 구글 로그인 하면 구글에서 리다이렉트한 코드를 받아오기
	// 2. 받아온 코드를 통해서 구글에서 제공하는 토큰을 받아오기.
	// 3. 반환 받은 토큰을 통해서 사용자 정보 가져오기. [전반적인 로직]
	// 참고)  https://darrenlog.tistory.com/40

	// 구글의 리다이렉트한 전송환 코드를 통해 토큰을 가져오기
	public String getGoogleAccessToken(String code) {
		String clientId = "720876072203-9qs394kg6d2ekko35ln9h0pil109lvft.apps.googleusercontent.com"; // 클라이언트 ID
		String clientSecret = "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"; // 클라이언트 비밀번호
		String redirectUri = "http://localhost:8080/api/auth/google"; // 다시 전달 받을 uri 주소
		String tokenUri = "https://oauth2.googleapis.com/token"; // 토큰을 가지고 있는 주소

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("code", code);
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("redirectUri", redirectUri);
		params.add("grant_type", "authorization_code"); // 어플리케이션이 authorization_code 유형을 사용한다고 명시.

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity entity = new HttpEntity(params, headers);

		ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity,
				JsonNode.class);
		JsonNode accessTokenNode = responseNode.getBody();
		return accessTokenNode.get("access_token").asText();

	}

	// 구글에서 전달 받은 토큰을 가지고 로그인한 구글 사용자 정보를 반환하기
	public JsonNode getGoogleUserInfo(String accessToken) {
		String resouceUri = "https://www.googleapis.com/oauth2/v2/userinfo";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer" + accessToken);
		HttpEntity entity = new HttpEntity(headers);
		return restTemplate.exchange(resouceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
	}

}

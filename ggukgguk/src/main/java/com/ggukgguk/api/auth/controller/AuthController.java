package com.ggukgguk.api.auth.controller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.auth.service.AuthService;
import com.ggukgguk.api.auth.service.OAuthService;
import com.ggukgguk.api.auth.vo.AuthTokenPayload;
import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.member.service.MemberService;
import com.ggukgguk.api.member.vo.Member;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	private AuthService service;
	@Autowired
	private MemberService memberSerivce;
	
	@Autowired
	private OAuthService oauth;
	
	// 일반 로그인 방식
	@PostMapping(value = "/login")
	public ResponseEntity<?> loginHandler(@RequestBody Member reqLoginInfo) {
		BasicResp<Object> respBody = null;

		final AuthTokenPayload payload = service.login(reqLoginInfo);

		if (payload == null) {
			log.debug("로그인 실패");
			respBody = new BasicResp<Object>("error", "로그인에 실패하였습니다.", null);
			return ResponseEntity.badRequest().body(respBody);
		} else {
			log.debug("로그인 성공");
			respBody = new BasicResp<Object>("success", null, payload);
			return ResponseEntity.ok(respBody);
		}
	}
	
	@PostMapping(value = "/refresh")
	public ResponseEntity<?> verifyHandler(@RequestBody HashMap<String, String> reqPayload) {

		AuthTokenPayload respPayload = service.regenToken(reqPayload.get("refreshToken"));

		BasicResp<?> respBody = null;
		int respCode = 0;
		if (respPayload == null) {
			respBody = new BasicResp<Object>("error", "토큰을 재발급하는데 실패했습니다.", null);
			return ResponseEntity.badRequest().body(respBody);
		} else {
			respBody = new BasicResp<Object>("success", null, respPayload);
			return ResponseEntity.ok(respBody);
		}
	}
	
	// 만약 프런트에서  Rest_API_Key와 Redirect url 주소를 반환하여 접근 코드를 건네줌 => 해당 코드를 접근 토큰으로 반환하여 백엔드 전송.
	// 1. kauth.kakao.com/oauth/authorize?client_id={Rest_API키}&redirect_uri={Redirect URI 주소}&response_type=code
	//kauth.kakao.com/oauth/authorize?client_id=88ae00c6ba4b777f197c6d3b5c972acd&redirect_uri=http://localhost:8080/api/auth/kakao&response_type=code
	// 
	//http://localhost:8080/api/auth/kakao?code=NFA9gSuZgIwCUkHC1306Mlndn6ilaRhvSMRboppO3jbm1gjIsn2def9KzxfsZaNtoft66Qo9c-sAAAGHk3H7yg
	
	// 백엔드에서는 
	// 서버에서 받은 access_token을 이용하여 카카오 서버에서 사용자 정보를 받음
	// 2. 받은 사용자 정보를 이용하여 회원가입 또는 로그인을 진행함
	// 참고 주소 : https://suyeoniii.tistory.com/79

	// 카카오 로그인 방식
	@PostMapping(value="/kakao")   //kakao의 접근 토큰을 반환하는지 메서드 
	public ResponseEntity<?> kakaoCallback(@RequestParam String code) throws Exception {
		BasicResp<Object> respBody;	
		String access_Token = oauth.getKakaoAccessToken(code); // 접근 토큰을 받아오기 위해 먼저 카카오에서 전송해준 리다이렉트한 코드를 통해 받아옴.
		HashMap<String, Object> userInfo = oauth.getKakaogetUserInfo(access_Token); // 해당 토큰을 통해서 사용자의 이메일과 별칭을 받아옴.
		
		if(userInfo != null) {
			log.debug("카카오 정보 반환 성공");
			respBody = new BasicResp<Object>("success", null, userInfo);
			System.out.println(userInfo);
			return ResponseEntity.ok(respBody);
		}else {
			log.debug("카카오 정보 반환 실패");
			respBody = new BasicResp<Object>("error", "실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
		
	}
	// 구글 로그인 방식
	@PostMapping("/google")
	public ResponseEntity<?> googleCallback(@RequestParam String code) throws Exception{
		BasicResp<Object> respBody;
		String access_Token = oauth.getGoogleAccessToken(code);
		JsonNode resouce = oauth.getGoogleUserInfo(access_Token);
		
		if(resouce !=null) {
			log.debug("구글 정보 반환 성공");
			respBody = new BasicResp<Object>("success", "구글 정보를 반환합니다.", resouce);
			return ResponseEntity.ok(respBody);
		}else {
			log.debug("구글 정보 반환 실패");
			respBody = new BasicResp<Object>("error", "구글 정보 가져오기를 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	// 회원가입  컨트롤러
	@PostMapping("/register")
	public ResponseEntity<?> registerHandler(@RequestBody Member member){
		BasicResp<Object> respBody;
		boolean result = memberSerivce.enrollMember(member);
		if(result) {
			log.debug("회원 가입 등록");
			respBody = new BasicResp<Object>("success", "등록되었습니다.", result);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("회원 가입 실패");
			respBody = new BasicResp<Object>("error","등록되지 않았습니다.",null);
			return ResponseEntity.badRequest().body(respBody);
		}
		
	
		
	}
}

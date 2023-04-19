package com.ggukgguk.api.auth.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@Service
public class OAuthService {
	
	
	public String getKakaoAccessToken (String code) {
	    String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            
            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=88ae00c6ba4b777f197c6d3b5c972acd");
            sb.append("&redirect_uri=http://localhost:8080/api/auth/kakao");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();
            
            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
 
            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            
            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            
            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);
            
            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return access_Token;
	}
	
	//카카오에서  사용자 정보 가져오기 (접근 토큰을 통해 사용자 정보 받아오기)
	public HashMap<String, Object> getKakaogetUserInfo(String access_Token){
		
		// 요청하는 클라이언트마다 가진 정보가 다를 수 있기 때문에 HashMap으로 설정
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		String reqUrl = "https://kapi.kakao.com/v2/user/me";
		
		try {
			URL url = new URL(reqUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			// 요청에 필요한 Header에 포함될 내용
			conn.setRequestProperty("Authorization", "Bearer " + access_Token);
			
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = "";
			String result = "";
			
			while((line = br.readLine()) != null){
				result += line;
			}
			System.out.println("response body : " + result);
			userInfo.put("result", result);
			JsonParser parser = new JsonParser();
	        JsonElement element = parser.parse(result);
	        
	        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
	        JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
	     
	        userInfo.put("id",element.getAsJsonObject().get("id").getAsLong()); // 카카오가 생성한 임의의 id
	        userInfo.put("nickname", properties.getAsJsonObject().get("nickname").getAsString()); // 카카오 프로필 이름
	        userInfo.put("email", kakao_account.getAsJsonObject().get("email").getAsString()); // 카카오 로그인한 이메일 주소
	        userInfo.put("birthday", kakao_account.getAsJsonObject().get("birthday").getAsString()); // 카카오 프로필에 등록한 생일 (월.일)
			
		}catch (IOException e) {
			// TODO: handle exception
		}
		
		return userInfo;
	}
	
	//1. 구글 로그인 하면 구글에서 리다이렉트한 코드를 받아오기
	//2. 받아온 코드를 통해서 구글에서 제공하는 토큰을 받아오기.
	//3. 반환 받은 토큰을 통해서 사용자 정보 가져오기. [전반적인 로직]
	// https://darrenlog.tistory.com/40
	
	
//	//
//	public JsonNode googleLogin(String code) {
//		String accessToken = getGoogleAccessToken(code);
//		System.out.println(accessToken);
//		JsonNode userResourceNode = getGoogleUserInfo(accessToken);
//		return userResourceNode;
//
////	        String id = userResourceNode.get("id").asText();
////	        String email = userResourceNode.get("email").asText();
////	        String nickname = userResourceNode.get("name").asText();
////	        System.out.println("id = " + id);
////	        System.out.println("email = " + email);
////	        System.out.println("nickname = " + nickname);
//		
//	}
	
	@Autowired
	private Environment env;
	
    RestTemplate restTemplate = new RestTemplate();
	
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
		params.add("grant_type", "authorization_code");
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);


        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
		
	}
	

	// 구글에서 전달 받은 토큰을 가지고 로그인한 구글 사용자 정보를 반환하기
	public JsonNode getGoogleUserInfo(String accessToken) {
		String resouceUri = "https://www.googleapis.com/oauth2/v2/userinfo";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization","Bearer" + accessToken);
		HttpEntity entity = new HttpEntity(headers);
		return restTemplate.exchange(resouceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
	}
	
}

package com.ggukgguk.api.auth.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private List<String> allowOrigins;
	
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    	System.out.println(allowOrigins);
    	
    	String requestOrigin = request.getHeader("Origin");
    	if (allowOrigins.contains(requestOrigin)) {
    		response.setHeader("Access-Control-Allow-Origin", requestOrigin);
    	}
    	
		response.setContentType("application/json; charset=UTF-8"); 
		
		response.setStatus(HttpServletResponse.SC_OK);
		
		PrintWriter out = response.getWriter();
		out.print("{ \"status\": \"error\", \"message\": \"로그인이 필요한 서비스입니다.\" }");
		out.close();
    }
}
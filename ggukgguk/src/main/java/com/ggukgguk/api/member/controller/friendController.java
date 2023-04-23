package com.ggukgguk.api.member.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.auth.service.AuthService;
import com.ggukgguk.api.auth.vo.AuthTokenPayload;
import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.member.service.MemberService;
import com.ggukgguk.api.member.vo.FriendRequest;
import com.ggukgguk.api.member.vo.Frined;
import com.ggukgguk.api.member.vo.Member;
import com.nimbusds.jose.Payload;

@RestController
@RequestMapping("/friend")
public class friendController {
	private Logger log = LogManager.getLogger("base");
	
	AuthTokenPayload Payload;
	
	@Autowired
	private MemberService memberservice;
	
	//친구 요청 
	@PostMapping(value = "/{toMemberid}") 
	public ResponseEntity<?> friendHandler(@PathVariable String toMemberid, @RequestBody FriendRequest request) {
		BasicResp<Object> respBody;
		request.setToMemberId(toMemberid);
		log.debug(request);
		boolean result = memberservice.requestFriend(request);
		if (result) {
			respBody = new BasicResp<Object>("success", "친구 요청을 성공하였습니다.", result);
			return ResponseEntity.ok(respBody);
		} else {
			respBody = new BasicResp<Object>("error", "친구 요청에 실패하였습니다.", null);
			return ResponseEntity.badRequest().body(respBody);
		}

	}
}

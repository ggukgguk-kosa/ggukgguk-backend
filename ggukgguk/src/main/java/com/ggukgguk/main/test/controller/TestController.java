package com.ggukgguk.main.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.main.test.service.MemberService;
import com.ggukgguk.main.test.vo.Member;

@RestController
@RequestMapping("/test")
public class TestController {

	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	MemberService service;
	
	@GetMapping("/login")
	public ResponseEntity<?> loginTest(@RequestParam("id") String memberId, @RequestParam("pw") String memberPw) {
		log.debug("로그인 테스트 접속");
		
		Member member = service.findMemberById(memberId);
		
		Map<String, Object> respBody = new HashMap<String, Object>();
		log.debug(member);
		if (member != null && member.getMemberPw().equals(memberPw)) {
			log.debug("로그인 테스트 성공");
			respBody.put("status", "success");
			respBody.put("message", "자격 증명에 성공했습니다.");
		} else {
			log.debug("로그인 테스트 실패");
			respBody.put("status", "failed");
			respBody.put("message", "자격 증명에 실패했습니다.");
		}
		
		return ResponseEntity.ok(respBody);
	}
}

package com.ggukgguk.api.admin.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.admin.service.AdminService;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.common.vo.BasicResp;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	AdminService service;
	
	@PostMapping("/notice")
	public ResponseEntity<?> writeNoticeHandler(@RequestBody Notice notice) {
		BasicResp<Object> respBody;
		boolean result = service.addNotice(notice);
		
		if (result) {
			log.debug("게시글 작성 성공");
			respBody = new BasicResp<Object>("success", null, null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 작성 실패");
			respBody = new BasicResp<Object>("error", "게시글 작성에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	@GetMapping("/notice")
	public ResponseEntity<?>
		getNoticeListHandler(@ModelAttribute NoticeOption option) {
		
		BasicResp<Object> respBody;
		List<Notice> result = service.getNoticeList(option);
		
		if (result != null) {
			log.debug("게시글 리스트 조회 성공");
			respBody = new BasicResp<Object>("success", null, result);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 리스트 조회 실패");
			respBody = new BasicResp<Object>("error", "게시글 조회에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
}

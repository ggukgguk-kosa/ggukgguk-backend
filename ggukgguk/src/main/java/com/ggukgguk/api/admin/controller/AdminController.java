package com.ggukgguk.api.admin.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.admin.service.AdminService;
import com.ggukgguk.api.admin.vo.Notice;
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
		service.addNotice(notice);
		log.debug("게시글 작성 성공");
		respBody = new BasicResp<Object>("success", null, null);
		return ResponseEntity.ok(respBody);
	}
}

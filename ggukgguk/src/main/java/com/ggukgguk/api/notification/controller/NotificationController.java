package com.ggukgguk.api.notification.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.notification.service.NotificationService;
import com.ggukgguk.api.notification.vo.Notification;

@RestController
@RequestMapping(value = "/notification")
public class NotificationController {

	private Logger log = LogManager.getLogger("base");

	@Autowired
	private NotificationService service;
	
	// 테스트용도
	// 알림 생성 // 근데. 알림은 생일, 조각, 월별 , 교환 알림이 있으므로 서비스임플 클래스에 구체적으로 생성.
	@PostMapping(value = "/{notificationTypeId}")
	public ResponseEntity<?> notifyInsert(@ModelAttribute Notification notification,@PathVariable String notificationTypeId, Authentication authentication){
		BasicResp<Object> respBody;
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String receiverId = userDetails.getUsername(); // 로그인한 상대방 ID
		int referenceId = 2;
		
		boolean result = service.createNotification(notification,receiverId,notificationTypeId,referenceId);
		
		if (result) {
			respBody = new BasicResp<Object>("success", "알림 생성을 성공하였습니다.", result);
			return ResponseEntity.ok(respBody);
		} else {
			respBody = new BasicResp<Object>("error", "알림 생성을 실패하였습니다.", result);
			return ResponseEntity.badRequest().body(respBody);
		}

	}
	
	// 회원별 알림 리스트 조회
	@GetMapping(value = "")
	public ResponseEntity<?> notifyListHandler(Authentication authentication){
		BasicResp<Object> respBody;
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String receiverId = userDetails.getUsername(); // 로그인한 본인 아이디
		
		List<Notification> result = service.ListNotification(receiverId);
		
		if (!result.equals(null)) {
			respBody = new BasicResp<Object>("success", "알림 조회를 성공하였습니다.", result);
			return ResponseEntity.ok(respBody);
		} else {
			respBody = new BasicResp<Object>("error", "알림 조회를 실패하였습니다.", null);
			return ResponseEntity.badRequest().body(respBody);
		}

	}
	
	// 알림 삭제
	@DeleteMapping(value = "{notificationId}")
	public ResponseEntity<?> notifyDeleteHandler(@PathVariable int notificationId, Authentication authentication){
		BasicResp<Object> respBody;
		
		boolean result = service.deleteNotify(notificationId);
		
		if (result) {
			respBody = new BasicResp<Object>("success", "알림 삭제를 성공하였습니다.", result);
			return ResponseEntity.ok(respBody);
		} else {
			respBody = new BasicResp<Object>("error", "알림 삭제를 실패하였습니다.", result);
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
}

package com.ggukgguk.api.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.admin.service.AdminService;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.common.vo.PageOption;
import com.ggukgguk.api.diary.service.DiaryService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private Logger log = LogManager.getLogger("base");

	@Autowired
	AdminService service;
	
	@Autowired
	DiaryService diaryService;
	
	// 전체 게시글 리스트 조회
	@GetMapping("list")
	public ResponseEntity<?> getNoticeList(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		PageOption option= new PageOption();
		option.setPage(page);
		option.setSize(size);
		List<Notice> result = service.getListPaging(option);

		BasicResp<Object> respBody = null;
		int respCode = 0;

		if (result != null) {
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("list", result);

			respBody = new BasicResp<Object>("true", "게시글 조회 성공", payload);
			respCode = HttpServletResponse.SC_OK;
		} else {
			respBody = new BasicResp<Object>("false", "게시글 조회 실패", null);
			respCode = HttpServletResponse.SC_BAD_REQUEST;
		}

		return new ResponseEntity<Object>(respBody, null, respCode);
	}
	
	// 게시글 작성
	@PostMapping("/write")
	public ResponseEntity<?> writeNoticeHandler(@RequestBody Notice notice) {
		BasicResp<Object> respBody;
		boolean result = service.addNotice(notice);

		if (result) {
			log.debug("게시글 작성 성공");
			respBody = new BasicResp<Object>("success", "게시글 작성에 실패하였습니다", null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 작성 실패");
			respBody = new BasicResp<Object>("error", "게시글 작성에 실패하였습니다", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	// 게시글 읽기
	@GetMapping("/read/{noticeId}")
	public ResponseEntity<?> readNoticeHanlder (@PathVariable int noticeId){
		BasicResp<Object> respBody;
		boolean result = service.readNotice(noticeId);
		
		if(result) {
			log.debug("게시글 읽기 성공");
			respBody = new BasicResp<Object>("success", "게시글 읽기 성공", null);
			return ResponseEntity.ok(respBody);
		}
		else {
			log.debug("게시글 읽기 실패");
			respBody = new BasicResp<Object>("error", "게시글 읽기에 실패하였습니다", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	// 게시글 수정
	@PutMapping("/update/{noticeId}")
	public ResponseEntity<?> updateNoticeHyandler (@PathVariable int noticeId){
		BasicResp<Object> respBody;
		boolean result = service.updateNotice(noticeId);

		if(result) {
			log.debug("게시글 수정 성공");
			respBody = new BasicResp<Object>("success", "게시글 수정 성공", null);
			return ResponseEntity.ok(respBody);
		}
		else {
			log.debug("게시글 수정 실패");
			respBody = new BasicResp<Object>("error", "게시글 수정 실패", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	@DeleteMapping("/delete/{noticeId}")
	public ResponseEntity<?> deleteNoticeHandler (@PathVariable int noticeId){
		BasicResp<Object> respBody;
		boolean result = service.deleteNotice(noticeId);

		if(result) {
			log.debug("게시글 삭제 성공");
			respBody = new BasicResp<Object>("success", "게시글 삭제 성공", null);
			return ResponseEntity.ok(respBody);
		}
		else {
			log.debug("게시글 삭제 실패");
			respBody = new BasicResp<Object>("error", "게시글 삭제 실패", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
//	// 미디어 파일 리스트 조회
//	// [GET] /api/admin/media ?페이지네이션 관련 패러미터
//	@GetMapping("/media")
//	public ResponseEntity<?> getDiaryInfo(@ModelAttribute PageOption option) {
//		log.debug(option);
//
//		BasicResp<Object> respBody = null;
//		int respCode = 0;
//		
//		List<Notice> result = service.getMediaList(option);
//
//		if (result != null) {
//			Map<String, Object> payload = new HashMap<String, Object>();
//			payload.put("list", result);
//
//			respBody = new BasicResp<Object>("true", "미디어 파일 조회 성공", payload);
//			respCode = HttpServletResponse.SC_OK;
//		} else {
//			respBody = new BasicResp<Object>("false", "미디어 파일 조회 실패", null);
//			respCode = HttpServletResponse.SC_BAD_REQUEST;
//		}
//
//		return new ResponseEntity<Object>(respBody, null, respCode);
//		
//		return null;
////		BasicResp<Object> respBody;
////		boolean result = service.getMediaList();
//
////		if(result) {
////			log.debug("게시글 삭제 성공");
////			respBody = new BasicResp<Object>("success", "게시글 삭제 성공", null);
////			return ResponseEntity.ok(respBody);
////		}
////		else {
////			log.debug("게시글 삭제 실패");
////			respBody = new BasicResp<Object>("error", "게시글 삭제 실패", null);
////			return ResponseEntity.badRequest().body(respBody);
////		}
//	}
//	
}
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.admin.service.AdminService;
import com.ggukgguk.api.admin.vo.Content;
import com.ggukgguk.api.admin.vo.ContentDetail;
import com.ggukgguk.api.admin.vo.Main;
import com.ggukgguk.api.admin.vo.Member;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.common.vo.PageOption;
import com.ggukgguk.api.diary.service.DiaryService;
import com.ggukgguk.api.record.service.RecordService;
import com.ggukgguk.api.record.service.ReplyService;
import com.ggukgguk.api.record.vo.Reply;
import com.ggukgguk.api.record.vo.ReplyNickname;

@RestController
@RequestMapping("/admin")
public class AdminController {
	private Logger log = LogManager.getLogger("base");

	@Autowired
	AdminService adminService;

	@Autowired
	DiaryService diaryService;
	
	@Autowired
	ReplyService replyService;
	
	@Autowired
	RecordService recordService;
	
	// admin main페이지
	@GetMapping("")
	public ResponseEntity<?> noticeaddHandler() {
		BasicResp<Object> respBody;
		Main result = adminService.mainAdmin();
		log.debug(result);
		if (result != null) {
			log.debug("admim page 조회 성공");
			respBody = new BasicResp<Object>("success", "admim page 조회 성공", result);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("admim page 조회 실패");
			respBody = new BasicResp<Object>("error", "admim page 조회 실패", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}

	// 전체 게시글 리스트 조회
	@GetMapping("/notice/list")
	public ResponseEntity<?> noticeSelectPageHandler(@RequestParam("page") int page, @RequestParam("size") int size) {
		PageOption option = new PageOption();
		option.setPage(page);
		option.setSize(size);
		List<Notice> result = adminService.noticeSelectPage(option);

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

	// 공지사항 게시글 작성
	@PostMapping("/notice/write")
	public ResponseEntity<?> noticeWirteHandler(@RequestBody Notice notice) {
		BasicResp<Object> respBody;
		boolean result = adminService.noticeWrite(notice);
		
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

	// 공지사항 게시글 읽기
	@GetMapping("/notice/read/{noticeId}")
	public ResponseEntity<?> noticeReadHandler(@PathVariable int noticeId) {
		BasicResp<Object> respBody;
		boolean result = adminService.noticeRead(noticeId);

		if (result) {
			log.debug("게시글 읽기 성공");
			respBody = new BasicResp<Object>("success", "게시글 읽기 성공", null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 읽기 실패");
			respBody = new BasicResp<Object>("error", "게시글 읽기에 실패하였습니다", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}

	// 공지사항 게시글 수정
	@PutMapping("/notice/update/{noticeId}")
	public ResponseEntity<?> noticeUpdateHandler(@PathVariable int noticeId) {
		BasicResp<Object> respBody;
		boolean result = adminService.noticeUpdate(noticeId);

		if (result) {
			log.debug("게시글 수정 성공");
			respBody = new BasicResp<Object>("success", "게시글 수정 성공", null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 수정 실패");
			respBody = new BasicResp<Object>("error", "게시글 수정 실패", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}

	// 공지사항 게시글 삭제
	@DeleteMapping("notice/delete/{noticeId}")
	public ResponseEntity<?> noticeDeleteHandler(@PathVariable int noticeId) {
		BasicResp<Object> respBody;
		boolean result = adminService.noticeDelete(noticeId);

		if (result) {
			log.debug("게시글 삭제 성공");
			respBody = new BasicResp<Object>("success", "게시글 삭제 성공", null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 삭제 실패");
			respBody = new BasicResp<Object>("error", "게시글 삭제 실패", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	// 컨텐츠관리 리스트 조회
	@GetMapping("/content")
	public ResponseEntity<?> contentListHandler(@RequestParam("page") int page, @RequestParam("size") int size) {
		PageOption option = new PageOption();
		option.setPage(page);
		option.setSize(size);
		List<Content> result = adminService.contentSelectPage(option);
		
		BasicResp<Object> respBody = null;
		int respCode = 0;

		if (result != null) {
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("list", result);

			respBody = new BasicResp<Object>("true", "컨텐츠 조회 성공", payload);
			respCode = HttpServletResponse.SC_OK;
		} else {
			respBody = new BasicResp<Object>("false", "컨텐츠 조회 실패", null);
			respCode = HttpServletResponse.SC_BAD_REQUEST;
		}

		return new ResponseEntity<Object>(respBody, null, respCode);
	}	
	
	// 회원관리 리스트 조회
	@GetMapping("/member")
	public ResponseEntity<?> membertListHandler(@RequestParam("page") int page, @RequestParam("size") int size) {
		PageOption option = new PageOption();
		option.setPage(page);
		option.setSize(size);
		List<Member> result = adminService.memberSelectPage(option);
		
		BasicResp<Object> respBody = null;
		int respCode = 0;

		if (result != null) {
			Map<String, Object> payload = new HashMap<String, Object>();
			payload.put("list", result);

			respBody = new BasicResp<Object>("true", "멤버관리 조회 성공", payload);
			respCode = HttpServletResponse.SC_OK;
		} else {
			respBody = new BasicResp<Object>("false", "멤버관리 조회 실패", null);
			respCode = HttpServletResponse.SC_BAD_REQUEST;
		}

		return new ResponseEntity<Object>(respBody, null, respCode);
	}	
	
	// 회원 삭제 
	@PutMapping("/member/delete/{memberId}")
	public ResponseEntity<?> memberDeleteHandler(@PathVariable String memberId) {
		BasicResp<Object> respBody;
		boolean result = adminService.memberDelete(memberId);

		if (result) {
			log.debug("회원 삭제 성공");
			respBody = new BasicResp<Object>("success", "회원 삭제 성공", null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("회원 삭제 실패");
			respBody = new BasicResp<Object>("error", "회원 삭제 실패", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	// 조각 상세 조회
	@GetMapping("/content/{recordId}")
	public ResponseEntity<?> recordReadHandler(@PathVariable int recordId) {
		BasicResp<Object> respBody;
		List<ContentDetail> result = adminService.recordRead(recordId);

		if (result != null) {
			log.debug("게시글 읽기 성공");
			respBody = new BasicResp<Object>("success", "게시글 읽기 성공", result);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 읽기 실패");
			respBody = new BasicResp<Object>("error", "게시글 읽기에 실패하였습니다", null);
			return ResponseEntity.badRequest().body(respBody);
		}
	}


	
	
	// 조각 삭제
	@DeleteMapping("record/{recordId}")
	public ResponseEntity<?> removeRecord(@PathVariable int recordId){
		
		BasicResp<Object> respBody;
		boolean result = recordService.removeRecord(recordId);
		
		if (result) {
			log.debug("게시글 삭제 성공");
			respBody = new BasicResp<Object>("success", null, null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 삭제 실패");
			respBody = new BasicResp<Object>("error", "게시글 삭제에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	// 댓글 삭제
	@DeleteMapping("/reply/{replyId}")
	public ResponseEntity<?> removeReply(@PathVariable int replyId, @RequestBody Reply reply){
	
		reply.setReplyId(replyId);
		BasicResp<Object> respBody;
		List<ReplyNickname> replyList = replyService.removeReply(reply);
		log.debug("삭제컨트롤러");
		
		if (replyList != null) {
			log.debug("댓글 삭제 성공");
			respBody = new BasicResp<Object>("success", null, replyList);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("댓글 삭제 실패");
			respBody = new BasicResp<Object>("error", "댓글 삭제에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
}

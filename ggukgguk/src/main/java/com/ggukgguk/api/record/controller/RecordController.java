package com.ggukgguk.api.record.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.record.service.RecordService;
import com.ggukgguk.api.record.service.ReplyService;
import com.ggukgguk.api.record.vo.Record;
import com.ggukgguk.api.record.vo.RecordSearch;
import com.ggukgguk.api.record.vo.Reply;
import com.ggukgguk.api.record.vo.ReplyNickname;

@RestController
@RequestMapping(value="/record")
public class RecordController {
	
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	RecordService service;
	@Autowired
	ReplyService rservice;
	
	@GetMapping
	public ResponseEntity<?> getRecords(@ModelAttribute RecordSearch recordSearch,
			@RequestParam(value = "startDateStr", required = false) String startDateStr){
			// date=2023-04-17 로 넣어줘야 한다. 날짜를 따옴표로 감싸면 안된다.
		
		log.debug(recordSearch);
		BasicResp<Object> respBody;		
		
		 Date startDate = null;
		    if (startDateStr != null && !startDateStr.isEmpty()) {
		        try {
		            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
		            recordSearch.setStartDate(startDate);
		        } catch (Exception e) {
		            // startDate 파라미터가 유효하지 않을 경우 처리
		        	e.printStackTrace();
		        	log.debug("게시글 리스트 조회 실패");
					respBody = new BasicResp<Object>("error", "날짜 형식이 잘 못되었습니다.", null);		
					return ResponseEntity.badRequest().body(respBody);
		        }
		    }
		    
		log.debug(recordSearch);
		
		if(recordSearch.getMemberId() == null) {
			log.debug("게시글 리스트 조회 실패");
			respBody = new BasicResp<Object>("error", "memberId는 필수값입니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		} else if(recordSearch.getKeyword()!= null && recordSearch.getStartDate() != null) {
			log.debug("게시글 리스트 조회 실패");
			respBody = new BasicResp<Object>("error", "날짜와 키워드를 동시에 넣을 수 없습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
		
		List<Record> recordList = service.getRecordList(recordSearch);
		
		if (recordList != null) {
			log.debug("게시글 리스트 조회 성공");
			
			respBody = new BasicResp<Object>("success", null, recordList);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 리스트 조회 실패");
			respBody = new BasicResp<Object>("error", "게시글 조회에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
		
	}

	@DeleteMapping(value="/{recordId}")
	public ResponseEntity<?> removeRecord(@PathVariable int recordId){
		
		BasicResp<Object> respBody;
		boolean result = service.removeRecord(recordId);
		
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
	
	@PostMapping("/reply")
	public ResponseEntity<?> addReply(@RequestBody Reply reply){
		
		BasicResp<Object> respBody;
		List<ReplyNickname> replyList = rservice.addReply(reply);
		
		if (replyList != null) {
			log.debug("댓글 등록 성공");
			
			respBody = new BasicResp<Object>("success", null, replyList);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("댓글 등록 실패");
			respBody = new BasicResp<Object>("error", "댓글 작성에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	@PutMapping("/reply/{replyId}")
	public ResponseEntity<?> editReply(@PathVariable int replyId, @RequestBody Reply reply){
		
		reply.setReplyId(replyId);
		BasicResp<Object> respBody;
		List<ReplyNickname> replyList = rservice.editReply(reply);
		
		if (replyList != null) {
			log.debug("댓글 수정 성공");
			respBody = new BasicResp<Object>("success", null, replyList);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("댓글 수정 실패");
			respBody = new BasicResp<Object>("error", "댓글 수정에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
		
	}
	
	@DeleteMapping("/reply/{replyId}")
	public ResponseEntity<?> removeReply(@PathVariable int replyId, @RequestBody Reply reply){
	
		reply.setReplyId(replyId);
		BasicResp<Object> respBody;
		List<ReplyNickname> replyList = rservice.removeReply(reply);
		
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

package com.ggukgguk.api.reply.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.reply.service.ReplyService;
import com.ggukgguk.api.reply.vo.Reply;
import com.ggukgguk.api.reply.vo.ReplyNickname;

@RestController
@RequestMapping(value="/record/reply")
public class ReplyController {

	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	private ReplyService service;
	
	@PostMapping
	public ResponseEntity<?> addReply(@RequestBody Reply reply){
		
		BasicResp<Object> respBody;
		List<ReplyNickname> replyList = service.addReply(reply);
		
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
	
	@PutMapping("/{replyId}")
	public ResponseEntity<?> editReply(@PathVariable int replyId, @RequestBody Reply reply){
		
		BasicResp<Object> respBody;
		List<ReplyNickname> replyList = service.editReply(reply);
		
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
	
	@DeleteMapping("/{replyId}")
	public ResponseEntity<?> removeReply(@PathVariable int replyId, @RequestBody Reply reply){
		
		BasicResp<Object> respBody;
		List<ReplyNickname> replyList = service.removeReply(reply);
		
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

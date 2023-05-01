package com.ggukgguk.api.record.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.member.service.MemberService;
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
	@Autowired
	MemberService mservice;
	
	@Value("${file.baseDir}")
	String baseDir;
	
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
		
		if(recordSearch.getMemberId() == null) {
			log.debug("게시글 리스트 조회 실패");
			respBody = new BasicResp<Object>("error", "memberId는 필수값입니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		} else if(recordSearch.getKeyword()!= null && recordSearch.getStartDate() != null) {
			log.debug("게시글 리스트 조회 실패");
			respBody = new BasicResp<Object>("error", "날짜와 키워드를 동시에 넣을 수 없습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	    
		List<Record> recordList = null;
		
		if(recordSearch.getFriendId() != null ) {
			int result = mservice.getFriendship(recordSearch);
			if(result == 1) {
				recordList = service.getFreindRecordList(recordSearch);
				log.debug(recordSearch);
			} else {
				log.debug("게시글 리스트 조회 실패");
				respBody = new BasicResp<Object>("error", "친구 관계가 아닙니다.", null);		
				return ResponseEntity.badRequest().body(respBody);
			}
		} else {
			recordList = service.getRecordList(recordSearch);
		}
		
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
	
	@PostMapping
	public ResponseEntity<?> addRecord(@RequestParam(value = "mediaFile", required = false) MultipartFile media,
			@ModelAttribute Record record,
			Authentication authentication) {
		
		BasicResp<Object> respBody;
		
		String memberIdFromReq = record.getMemberId();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		if (!userDetails.getUsername().equals(memberIdFromReq)) {
			log.debug("조각 INSERT 실패 1");
			respBody = new BasicResp<Object>("error", "새로운 조각 업로드에 실패하였습니다. (ID_NOT_VERIFIED)", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
		
		boolean result = service.saveMediaAndRecord(media, record);
		
		if (result) {
			log.debug("조각 INSERT 성공");
			respBody = new BasicResp<Object>("success", null, null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("조각 INSERT 실패 2");
			respBody = new BasicResp<Object>("error", "새로운 조각 업로드에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	@GetMapping(value="/media/{fileId}")
	public ResponseEntity<InputStreamResource> getImageMedia(@PathVariable("fileId") String fileId, @RequestParam("mediaType") String mediaType) throws IOException {	
		MediaType contentsType;
		String subDir;
		switch (mediaType) {
		case "image":
			contentsType = MediaType.IMAGE_JPEG;
			subDir = "image";
			break;
		case "audio":
			contentsType = new MediaType("audio", "wav");
			subDir = "audio";
			break;
		case "video":
			contentsType = new MediaType("video", "mp4");
			subDir = "video";
			break;
		default:
			contentsType = MediaType.APPLICATION_OCTET_STREAM;
			subDir = "";
			break;
		}
		
		File mediaFile = new File(baseDir + "/" + subDir + "/" + fileId);
		File defaultFile = new File(baseDir + "/" + subDir + "/default");
		InputStream in;
		try {
			in = new FileInputStream(mediaFile);
		    return ResponseEntity.ok()
		    	      .contentType(contentsType)
		    	      .body(new InputStreamResource(in));
		} catch (FileNotFoundException e) {
			log.debug("미디어 파일을 찾을 수 없습니다.");
			e.printStackTrace();
			in = new FileInputStream(defaultFile);
		    return ResponseEntity.ok()
		    	      .contentType(contentsType)
		    	      .body(new InputStreamResource(in));
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
		
		log.debug(reply);
		
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
		
		log.debug("여기로 연결되니?");
		log.debug(reply);
		reply.setReplyId(replyId);
		log.debug(reply);
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

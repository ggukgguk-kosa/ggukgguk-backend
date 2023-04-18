package com.ggukgguk.api.record.controller;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.record.service.RecordService;
import com.ggukgguk.api.record.vo.Record;

@RestController
@RequestMapping(value="/record")
public class RecordController {
	
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	RecordService service;
	
	@GetMapping
	public ResponseEntity<?> getRecords(@RequestParam("memberId") String memberId,
			@RequestParam("date") Date recordCreateAt,
	        @RequestParam(value = "year", required = false) Integer year,
	        @RequestParam(value = "month", required = false) Integer month,
	        @RequestParam(value = "keyword", required = false) String keyword){
		
		Record record = new Record();
		record.setMemberId(memberId);
		record.setRecordCreateAt(recordCreateAt);
		
		BasicResp<Object> respBody;		
		List<Record> recordList = service.getRecordList(record);
		
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
	
	
	
}

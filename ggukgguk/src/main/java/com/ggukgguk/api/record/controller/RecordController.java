package com.ggukgguk.api.record.controller;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.record.service.RecordService;
import com.ggukgguk.api.record.vo.Record;

@RestController
@RequestMapping(value="/api/record")
public class RecordController {
	
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	RecordService service;
	
	@GetMapping
	public ResponseEntity<?> getRecords(@RequestParam("memberId") String memberId,
			@RequestParam("date") Date recordCreateAt){
		
		Record record = new Record();
		record.setMemberId(memberId);
		record.setRecordCreateAt(recordCreateAt);
		
		BasicResp<Object> respBody;		
		TotalAndListPayload payload = service.getRecordList(record);
		
		if (payload != null) {
			log.debug("게시글 리스트 조회 성공");
			
			respBody = new BasicResp<Object>("success", null, payload);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("게시글 리스트 조회 실패");
			respBody = new BasicResp<Object>("error", "게시글 조회에 실패하였습니다.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
		
	}

}

package com.ggukgguk.api.record.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.record.service.RecordService;
import com.ggukgguk.api.record.vo.Record;

@RestController
@RequestMapping(value="/record")
public class RecordController {
	
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	RecordService service;
	
	@Value("${file.baseDir}")
	String baseDir;
	
	@GetMapping
	public ResponseEntity<?> getRecords(@RequestParam("memberId") String memberId,
			@RequestParam("date") Date recordCreatedAt,
	        @RequestParam(value = "year", required = false) Integer year,
	        @RequestParam(value = "month", required = false) Integer month,
	        @RequestParam(value = "keyword", required = false) String keyword){
		
		Record record = new Record();
		record.setMemberId(memberId);
		record.setRecordCreatedAt(recordCreatedAt);
		
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
	
	@PostMapping
	public ResponseEntity<?> addRecord(@RequestParam("mediaFile") MultipartFile media,
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
	
	@GetMapping(value="/media/{fileId}}", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImageMedia(@PathVariable String fileId, @RequestParam("mediaType") String mediaType) throws IOException {	
		MediaType contentsType;
		String subDir;
		switch (mediaType) {
		case "image":
			contentsType = MediaType.IMAGE_JPEG;
			subDir = "image";
			break;
		case "audio":
			contentsType = new MediaType("audio/wav");
			subDir = "audio";
			break;
		case "video":
			contentsType = new MediaType("video/mp4");
			subDir = "video";
			break;
		default:
			subDir = "";
			break;
		}
		
		File mediaFile = new File(baseDir + "/" + subDir + "/" + fileId);
		File defaultFile = new File(baseDir + "/" + subDir + "/default");
		InputStream in;
		try {
			in = new FileInputStream(mediaFile);
			return IOUtils.toByteArray(in);
		} catch (FileNotFoundException e) {
			log.debug("미디어 파일을 찾을 수 없습니다.");
			e.printStackTrace();
			in = new FileInputStream(defaultFile);
			return IOUtils.toByteArray(in);
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

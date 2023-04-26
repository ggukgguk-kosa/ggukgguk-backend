package com.ggukgguk.api.diary.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggukgguk.api.common.vo.BasicResp;
import com.ggukgguk.api.diary.service.DiaryService;
import com.ggukgguk.api.diary.vo.Diary;
import com.ggukgguk.api.diary.vo.DiaryColor;
import com.ggukgguk.api.diary.vo.DiaryMonth;
import com.ggukgguk.api.diary.vo.DiarySearch;

@RestController
@RequestMapping("/diary")
public class DiaryController {

	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	DiaryService service;
	
	@GetMapping
	public ResponseEntity<?> getDiaries(@ModelAttribute DiarySearch diarySearch){
		
		log.debug(diarySearch);
		
		BasicResp<Object> respBody;	
		
		if(diarySearch.getDiaryYear()==null) {
			log.debug("�떎�씠�뼱由� 由ъ뒪�듃 議고쉶 �떎�뙣1");
			respBody = new BasicResp<Object>("error", "�뿰�룄媛� 鍮꾩뼱�엳�뒿�땲�떎.", null);		
			return ResponseEntity.badRequest().body(respBody);
		} else if(diarySearch.getDiaryYear()!=null && diarySearch.getDiaryMonth()==null) {
			List<Diary> diaryList = service.getDiaries(diarySearch);
			if (diaryList != null) {
				log.debug("�떎�씠�뼱由� 由ъ뒪�듃 議고쉶 �꽦怨�2");
				respBody = new BasicResp<Object>("success", null, diaryList);
				return ResponseEntity.ok(respBody);
			} else {
				log.debug("�떎�씠�뼱由� 由ъ뒪�듃 議고쉶 �떎�뙣2");
				respBody = new BasicResp<Object>("error", "�떎�씠�뼱由� 議고쉶�뿉 �떎�뙣�븯���뒿�땲�떎.", null);		
				return ResponseEntity.badRequest().body(respBody);
			}
		} else if(diarySearch.getDiaryYear()!=null && diarySearch.getDiaryMonth()!=null) {
			DiaryMonth diaryMonth = service.getDiary(diarySearch);
			log.debug(diaryMonth);
			if (diaryMonth != null) {
				log.debug("�떎�씠�뼱由� 由ъ뒪�듃 議고쉶 �꽦怨�3");
				respBody = new BasicResp<Object>("success", null, diaryMonth);
				return ResponseEntity.ok(respBody);
			} else {
				log.debug("�떎�씠�뼱由� 由ъ뒪�듃 議고쉶 �떎�뙣3");
				respBody = new BasicResp<Object>("error", "�떎�씠�뼱由� 議고쉶�뿉 �떎�뙣�븯���뒿�땲�떎.", null);		
				return ResponseEntity.badRequest().body(respBody);
			}
		} else {
			log.debug("�떎�씠�뼱由� 由ъ뒪�듃 議고쉶 �떎�뙣4");
			respBody = new BasicResp<Object>("error", "�떎�씠�뼱由� 議고쉶�뿉 �떎�뙣�븯���뒿�땲�떎.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	// 異붿쿇 而щ윭 議고쉶
	@GetMapping("/{diaryId}")
	public ResponseEntity<?> getColors(@PathVariable int diaryId){
		
		BasicResp<Object> respBody;	
		List<DiaryColor> colorList = service.getColors(diaryId);
		if (colorList != null) {
			log.debug("�깋�긽 由ъ뒪�듃 議고쉶 �꽦怨�");
			respBody = new BasicResp<Object>("success", null, colorList);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("�깋�긽 由ъ뒪�듃 議고쉶 �떎�뙣");
			respBody = new BasicResp<Object>("error", "異붿쿇 �깋�긽 議고쉶�뿉 �떎�뙣�븯���뒿�땲�떎.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
	}
	
	
	// 硫붿씤 而щ윭 �닔�젙
	@PutMapping("/{diaryId}")
	public ResponseEntity<?> editDiary(@PathVariable int diaryId, @RequestBody Diary diary){
		
		log.debug(diary);
		BasicResp<Object> respBody;	
		
		boolean result = service.editDiary(diary);
		
		if(result) {
			log.debug("�떎�씠�뼱由� �닔�젙 �꽦怨�");
			respBody = new BasicResp<Object>("success", "�깋�긽 �닔�젙�뿉 �꽦怨듯뻽�뒿�땲�떎", null);
			return ResponseEntity.ok(respBody);
		} else {
			log.debug("寃뚯떆湲� �옉�꽦 �떎�뙣");
			respBody = new BasicResp<Object>("error", "�깋�긽 �닔�젙�뿉 �떎�뙣�븯���뒿�땲�떎.", null);		
			return ResponseEntity.badRequest().body(respBody);
		}
		
	}
	
}

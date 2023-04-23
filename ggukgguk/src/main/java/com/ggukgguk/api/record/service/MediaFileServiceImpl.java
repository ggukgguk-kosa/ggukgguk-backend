package com.ggukgguk.api.record.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaFileServiceImpl implements MediaFileService {
	
	private Logger log = LogManager.getLogger("base");
	private final int mediaWidth = 1200;

	@Value("${file.baseDir}")
	String baseDir;
	
	/**
	 * 미디어 파일을 저장한다.
	 */
	@Override
	public boolean saveFile(MultipartFile file, String subDir, String saveName) {
		File saveFile = new File(baseDir + "/" + subDir + "/" + saveName);
		try {
			file.transferTo(saveFile);
			log.debug("파일 저장 성공");
			
			if (prcessMedia(saveFile, file.getContentType())) {
				log.debug("파일 후처리 성공");
				return true;
			} else {
				log.debug("파일 후처리 실패");
				return false;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 미디어 파일을 저장한 후에 포맷 등을 통일하는 작업을 수행한다.
	 * @param file
	 * @param contentType
	 * @return
	 */
	private boolean prcessMedia(File file, String contentType) {
		String format = contentType.split("/")[0];
		boolean result = false;
		
		if (format != null) {
			switch (format) {
			case "image":
				log.debug("이미지 후처리");
				result = resizeImage(file);
				break;
			case "video":
				log.debug("비디오 후처리");
				result = true;
				// 여기에 후처리 로직을 추가
				break;
			case "audio":
				log.debug("오디오 후처리");
				result = true;
				// 여기에 후처리 로직을 추가
				break;
			default:
				log.debug("지원하지 않는 미디어 타입은 처리할 수 없습니다.");
				result = false;
			}
		}
		
		return result;
	}
	
	/*
	 * 이미지의 너비가 특정 수준을 넘어서면, 해당 수준에 맞게 리사이징한다.
	 */
	private boolean resizeImage(File imgFile) {
		// 원본 이미지 로드
		BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// 원본 이미지 사이즈 불러오기
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();
		
		// 원본이 이미 1200 픽셀보다 작으면 리사이징이 필요 없음
		if (originalWidth <= mediaWidth) return true;
		
		// 새 이미지 사이즈 계산
		int newWidth = mediaWidth;
		int newHeight = originalHeight * mediaWidth / originalWidth; // 새 높이 = 기존 높이 * 새 너비(고정) / 기존 너비

		// 리사이징 및 저장
		BufferedImage newImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
	    Graphics2D g = newImage.createGraphics();  
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
	    g.drawImage(originalImage, 0, 0, newWidth, newHeight, 0, 0, originalWidth, originalHeight, null);  
	    g.dispose();
	    
	    try {
			ImageIO.write(newImage, "jpg", imgFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}

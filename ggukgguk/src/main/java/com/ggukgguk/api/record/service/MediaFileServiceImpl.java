package com.ggukgguk.api.record.service;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaFileServiceImpl implements MediaFileService {
	
	private Logger log = LogManager.getLogger("base");

	@Value("${file.baseDir}")
	String baseDir;
	
	@Override
	public boolean saveFile(MultipartFile file, String subDir, String saveName) {
		File saveFile = new File(baseDir + "/" + subDir + "/" + saveName);
		try {
			file.transferTo(saveFile);
			log.debug("파일 저장 성공");
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}

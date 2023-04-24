package com.ggukgguk.api.record.service;

import java.util.List;
import java.util.UUID;

import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.record.dao.RecordDao;
import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.record.vo.Record;
import com.ggukgguk.api.record.vo.RecordSearch;

@Service
public class RecordServiceImpl implements RecordService{

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private RecordDao dao;
	
	@Autowired
	private MediaFileService mediaFileService;
	
	@Override
	public List<Record> getRecordList(RecordSearch recordSearch) {
		
		return dao.selectRecordList(recordSearch);
	}

	public boolean removeRecord(int recordId) {
		
		try {
			dao.deleteRecord(recordId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveMediaAndRecord(MultipartFile media, Record record) {
		String contentType = media.getContentType();

		String format = contentType.split("/")[0];
		String saveName = (UUID.randomUUID()).toString();
		MediaFile metadata = new MediaFile(saveName, format, false);
		record.setMediaFileId(saveName);
		
		boolean fileSaveResult = mediaFileService.saveFile(media, format, saveName);
		if (!fileSaveResult) return false;
		
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
        
    	try {
			dao.insertMediaFile(metadata);
			dao.insertRecord(record);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			e.printStackTrace();
			return false;
		}
    	
    	transactionManager.commit(txStatus);
		return true;
	}
}

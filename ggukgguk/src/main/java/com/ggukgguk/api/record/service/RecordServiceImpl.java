package com.ggukgguk.api.record.service;

import java.util.List;
import java.util.UUID;

import javax.management.Notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private Logger log = LogManager.getLogger("base");
	
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

	@Override
	public boolean updateRecord(Record record) {
		
		
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			dao.updateRecord(record);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			e.printStackTrace();
			return false;
		}
		
		transactionManager.commit(txStatus);
		return true;
	}
	
	public boolean removeRecord(int recordId) {
		
		Record record = dao.selectRecord(recordId);
		
		log.debug(record);
		
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
		
        
		if(!record.getReplyList().isEmpty()) {
			try {
				dao.deleteReplyList(record.getRecordId());
			} catch (Exception e) {
				transactionManager.rollback(txStatus);
				e.printStackTrace();
				return false;
			}
		}
		
		int keywordCount = dao.selectKeyword(record.getRecordId());
		
		if(keywordCount > 0) {
			try {
				dao.deleteKeyword(recordId);		
			} catch (Exception e) {
				transactionManager.rollback(txStatus);
				e.printStackTrace();
				return false;
			}		
		}
		
		try {
			dao.deleteRecord(recordId);
			
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			e.printStackTrace();
			return false;
		}
		
		if(record.getMediaFileId()!=null) {
			try {
				dao.deleteMediaFile(record.getMediaFileId());
			} catch (Exception e) {
				transactionManager.rollback(txStatus);
				e.printStackTrace();
				return false;
			}
		}
	
		transactionManager.commit(txStatus);
		return true;
	}

	@Override
	public boolean saveMediaAndRecord(MultipartFile media, Record record) {
		MediaFile metadata = null;

		if (media != null) {
			String contentType = media.getContentType();
			System.out.println(contentType);

			String format = contentType.split("/")[0];
			String saveName = (UUID.randomUUID()).toString();
			metadata = new MediaFile(saveName, format, false, false);
			record.setMediaFileId(saveName);

			boolean fileSaveResult = mediaFileService.saveFile(media, format, saveName);
			if (!fileSaveResult) return false;
		}
		
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

    	try {
			if (media != null) dao.insertMediaFile(metadata);
			dao.insertRecord(record);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			e.printStackTrace();
			return false;
		}
    	
    	transactionManager.commit(txStatus);
		return true;
	}
	
	@Override
	public List<Record> getFreindRecordList(RecordSearch recordSearch) {
		
		return dao.selectFriendRecordList(recordSearch);
	}
	
	@Override
	public List<Record> getUnaccepted(String memberId) {
		
		return dao.selectUnaccepted(memberId);
	}
	
	@Override
	public boolean updateUnaccepted(int recordId) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			dao.updateUnaccepted(recordId);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			e.printStackTrace();
			return false;
		}
		
		transactionManager.commit(txStatus);
		return true;
	}
}

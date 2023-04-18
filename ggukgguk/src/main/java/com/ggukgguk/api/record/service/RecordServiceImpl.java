package com.ggukgguk.api.record.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.record.dao.RecordDao;
import com.ggukgguk.api.record.vo.Record;

@Service
public class RecordServiceImpl implements RecordService{

	@Autowired
	private RecordDao dao;
	
	@Override
	public List<Record> getRecordList(Record record) {
		
		return dao.selectRecordList(record);
	}

	@Override
	public boolean saveFileAndMetadata(MultipartFile file, MediaFile metadata) {
		// TODO Auto-generated method stub
		return false;
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
}

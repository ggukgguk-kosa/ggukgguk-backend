package com.ggukgguk.api.record.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.record.vo.Record;

@Service
public class RecordServiceImpl implements RecordService{

	@Override
	public List<Record> getRecordList(Record record) {
		
		return null;
	}

	@Override
	public boolean saveFileAndMetadata(MultipartFile file, MediaFile metadata) {
		// TODO Auto-generated method stub
		return false;
	}

}

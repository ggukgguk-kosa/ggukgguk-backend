package com.ggukgguk.api.record.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.record.vo.Record;
import com.ggukgguk.api.record.vo.RecordSearch;

public interface RecordService {

	public List<Record> getRecordList(RecordSearch recordSearch);

	public boolean removeRecord(int recordId);
	
	public boolean saveMediaAndRecord(MultipartFile media, Record record);

}

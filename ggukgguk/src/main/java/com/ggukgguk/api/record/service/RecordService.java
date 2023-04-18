package com.ggukgguk.api.record.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.record.vo.Record;

public interface RecordService {

	public List<Record> getRecordList(Record record);
	public boolean saveFileAndMetadata(MultipartFile file, MediaFile metadata);
	List<Record> getRecordList(Record record);

	boolean removeRecord(int recordId);

}

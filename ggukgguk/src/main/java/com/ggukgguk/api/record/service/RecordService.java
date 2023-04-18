package com.ggukgguk.api.record.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.record.vo.Record;

public interface RecordService {

	public List<Record> getRecordList(Record record);
	public boolean saveFileAndMetadata(MultipartFile file, MediaFile metadata);
}

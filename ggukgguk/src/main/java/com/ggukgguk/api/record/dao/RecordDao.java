package com.ggukgguk.api.record.dao;

import java.util.List;

import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.record.vo.Record;

public interface RecordDao {

	List<Record> selectRecordList(Record record);

	void deleteRecord(int recordId) throws Exception;

	void insertMediaFile(MediaFile metadata) throws Exception;

	void insertRecord(Record record) throws Exception;

}

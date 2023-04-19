package com.ggukgguk.api.record.dao;

import java.util.List;

import com.ggukgguk.api.record.vo.MediaFile;
import com.ggukgguk.api.record.vo.Record;
import com.ggukgguk.api.record.vo.RecordSearch;

public interface RecordDao {

	List<Record> selectRecordList(RecordSearch recordSearch);

	void deleteRecord(int recordId) throws Exception;

	void insertMediaFile(MediaFile metadata) throws Exception;

	void insertRecord(Record record) throws Exception;

}

package com.ggukgguk.api.record.dao;

import java.util.List;

import com.ggukgguk.api.record.vo.Record;

public interface RecordDao {

	List<?> selectRecordList(Record record);

}

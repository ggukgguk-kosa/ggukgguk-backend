package com.ggukgguk.api.record.service;

import java.util.List;
import java.util.Map;

import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.record.vo.Record;

public interface RecordService {

	List<Record> getRecordList(Record record);

	boolean removeRecord(int recordId);

}

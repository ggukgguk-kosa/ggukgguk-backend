package com.ggukgguk.api.record.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.record.dao.RecordDao;
import com.ggukgguk.api.record.vo.Record;

@Service
public class RecordServiceImpl implements RecordService{

	@Autowired
	private RecordDao dao;
	
	@Override
	public TotalAndListPayload getRecordList(Record record) {
		
		TotalAndListPayload payload = new TotalAndListPayload();
		payload.setList(dao.selectRecordList(record));
		
		return payload;
	}

}

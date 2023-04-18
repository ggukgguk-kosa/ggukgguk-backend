package com.ggukgguk.api.record.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ggukgguk.api.record.vo.Record;

@Repository
public class RecordDaoImpl implements RecordDao{

	@Autowired
	SqlSession session;
	
	@Override
	public List<Record> selectRecordList(Record record) {
		
		return session.selectList("com.ggukgguk.api.Record.selectList", record);
	}
	
	@Override
	public void deleteRecord(int recordId) throws Exception {

		int affectedRow = session.delete("com.ggukgguk.api.Record.delete", recordId);
		
		if (affectedRow != 1) {
			throw new Exception();
		}
		
	}
}

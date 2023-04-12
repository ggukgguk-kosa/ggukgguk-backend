package com.ggukgguk.api.admin.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ggukgguk.api.admin.vo.Notice;

@Repository
public class AdminDaoImpl implements AdminDao {

	@Autowired
	SqlSession session;
	
	@Override
	public void insertNotice(Notice notice) throws Exception {
		int affectedRow = session.insert("com.ggukgguk.api.Notice.insert", notice);
		
		if (affectedRow != 1) {
			throw new Exception();
		}
	}

}

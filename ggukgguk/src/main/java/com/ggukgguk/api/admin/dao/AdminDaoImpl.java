package com.ggukgguk.api.admin.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.SearchCondition;

@Repository
public class AdminDaoImpl implements AdminDao {

	@Autowired
	private SqlSession session;
	String namespace = "com.ggukgguk.api.Notice.";

	public int count() throws Exception {
		return session.selectOne(namespace + "count");
	} // T selectOne(String statement)
	
	@Override
	public int delete(int noticeId, String writer) throws Exception {
		Map map = new HashMap();
		map.put("notice_id", noticeId);
		map.put("writer", writer);
		return session.delete(namespace + "delete", map);
	} // int delete(String statement, Object parameter)

	public int insert(Notice notice) throws Exception {
		return session.insert(namespace + "insert", notice);
	} // int insert(String statement, Object parameter)

	@Override
	public List<Notice> selectAll() throws Exception {
		return session.selectList(namespace + "selectAll");
	} // List<E> selectList(String statement)

	public Notice select(int noticeId) throws Exception {
		return session.selectOne(namespace + "select", noticeId);
	} // T selectOne(String statement, Object parameter)

	@Override
	public List<Notice> selectPage(Map map) throws Exception {
		return session.selectList(namespace + "selectPage", map);
	} // List<E> selectList(String statement, Object parameter)

	@Override
	public int update(Notice notice) throws Exception {
		return session.update(namespace + "update", notice);
	} // int update(String statement, Object parameter)

	@Override
	public int increaseViewCnt(int noticeId) throws Exception {
		return session.update(namespace + "increaseViewCnt", noticeId);
	} // int update(String statement, Object parameter)


//	@Override
//	public int deleteAll() {
//		return session.delete(namespace + "deleteAll");
//	} // int delete(String statement)

//	@Override
//	public int searchResultCnt(SearchCondition sc) throws Exception {
//		System.out.println("sc in searchResultCnt() = " + sc);
//		System.out.println("session = " + session);
//		return session.selectOne(namespace + "searchResultCnt", sc);
//	} // T selectOne(String statement, Object parameter)
//
//	@Override
//	public List<Notice> searchSelectPage(SearchCondition sc) throws Exception {
//		return session.selectList(namespace + "searchSelectPage", sc);
//	} // List<E> selectList(String statement, Object parameter)

}

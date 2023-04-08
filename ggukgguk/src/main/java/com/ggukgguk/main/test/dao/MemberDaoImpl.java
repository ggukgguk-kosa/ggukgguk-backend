package com.ggukgguk.main.test.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ggukgguk.main.test.vo.Member;

@Repository
public class MemberDaoImpl implements MemberDao {

	@Autowired
	private SqlSession session;
	
	@Override
	public Member selectMemberById(String memberId) {
		return session.selectOne("com.ggukgguk.main.Member.selectById", memberId);
	}

}

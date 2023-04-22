package com.ggukgguk.api.member.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ggukgguk.api.member.vo.Member;

@Repository
public class MemberDaoImpl implements MemberDao {
	
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	private SqlSession session;
	
	@Override
	public Member selectMemberById(String memberId) {
		return session.selectOne("com.ggukgguk.api.Member.selectById", memberId);
	}

	@Override // 회원 가입방식
	public void insertMember(Member member) throws Exception{
		log.debug("테스트");
		int count = session.insert("com.ggukgguk.api.Member.registerMember",member);
		
		if (count != 1) {
			throw new Exception();
		}
	}
	@Override // 아이디 찾기  = 이메일 값으로 부터 찾기
	public Member selectMemberByEmail(String memberEmail) {
		return session.selectOne("com.ggukgguk.api.Member.selectByEmail",memberEmail);
	}

	@Override // 비밀번호 찾기 
	public Member selectMemberByEmailandId(Member member) {
		return session.selectOne("com.ggukgguk.api.Member.selectByPassword", member);
	}
	

}

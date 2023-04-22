package com.ggukgguk.api.member.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.MementoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.member.dao.MemberDao;
import com.ggukgguk.api.member.vo.Member;

@Service
public class MemberServiceImpl implements MemberService {
	
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	private MemberDao dao;
	
	@Autowired
	private PasswordEncoder passwordEncorder;
	
	
	@Override
	// 아이디 중복검사 // 회원 정보 조회
	public Member findMemberById(String memberId) {
		return dao.selectMemberById(memberId);
	}

	//회원가입
	@Override
	public boolean enrollMember(Member member) {
		try {
			member.setMemberPw(passwordEncorder.encode(member.getMemberPw()));
			dao.insertMember(member);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	// 이메일 값으로 아이디 찾기.
	@Override
	public Member getMemberByEmail(String memberEmail) {
		return dao.selectMemberByEmail(memberEmail);
	}

	// 비밀번호 찾기 
	@Override
	public Boolean getMemberByEmailandId(Member member) {
		
	 Member user = dao.selectMemberByEmailandId(member);
	 if(!user.equals(null)) return true;
	 return false;
	}
	
}

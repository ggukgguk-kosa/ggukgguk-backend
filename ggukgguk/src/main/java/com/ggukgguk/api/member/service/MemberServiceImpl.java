package com.ggukgguk.api.member.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

}

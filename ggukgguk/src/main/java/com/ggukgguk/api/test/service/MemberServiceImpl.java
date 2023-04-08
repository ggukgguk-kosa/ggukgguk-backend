package com.ggukgguk.api.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.test.dao.MemberDao;
import com.ggukgguk.api.test.vo.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao dao;
	
	@Override
	public Member findMemberById(String memberId) {
		return dao.selectMemberById(memberId);
	}

}

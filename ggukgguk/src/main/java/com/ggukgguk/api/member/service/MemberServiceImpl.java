package com.ggukgguk.api.member.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.MementoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.member.dao.MemberDao;
import com.ggukgguk.api.member.vo.FriendRequest;
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
	public boolean getMemberByEmailandId(Member member) {
		
	 Member user = dao.selectMemberByEmailandId(member);
	 if(!user.equals(null)) return true;
	 return false;
	}

	// 회원정보 수정
	@Override
	public boolean modifyMember(Member member) {
		try {
			member.setMemberPw(passwordEncorder.encode(member.getMemberPw()));
			dao.updateMemberInfo(member);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 전체 회원 리스트 조회(관리자용)
	@Override
	public TotalAndListPayload getMemberList(NoticeOption option) {
		TotalAndListPayload payload = new TotalAndListPayload();
		payload.setList(dao.selectMemberList(option)); // 전체 회원 리스트 조회
		payload.setTotal(dao.selectMemberListTotal(option)); // 페이징 처리를 위한 전체회원 수 구하기
		return payload;
	}
	
	//친구 요청 
	@Override
	public boolean requestFriend(FriendRequest request) {
		try {
			dao.requestFriend(request);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}

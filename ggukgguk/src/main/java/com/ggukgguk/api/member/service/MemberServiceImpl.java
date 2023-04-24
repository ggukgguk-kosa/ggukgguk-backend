package com.ggukgguk.api.member.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.MementoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.member.controller.friendController;
import com.ggukgguk.api.member.dao.MemberDao;
import com.ggukgguk.api.member.vo.Friend;
import com.ggukgguk.api.member.vo.FriendRequest;
import com.ggukgguk.api.member.vo.Member;

@Service
public class MemberServiceImpl implements MemberService {
	
	private Logger log = LogManager.getLogger("base");
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
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
	
	//  친구 수락
	@Override
	public boolean acceptFriend(Friend friend, FriendRequest friendRequest,String toMemberId,String fromMemberId) {
		// 먼저 친구 요청 테이블에 현재 관계가 있는 지 여부 조회
		friendRequest.setFromMemberId(toMemberId); // ex)  (친구 요청을 한 사람 ) 손흥민
		friendRequest.setToMemberId(fromMemberId); // (ex) (친구를 요청 받은 사람) 홍길동
		log.debug(toMemberId);
		log.debug(friendRequest);
		FriendRequest result = dao.selectFriendRequestList(friendRequest);
		if(result.getFriendRequestId().equals(null)) return false;
		// 이후 트랜잭션 처리로 친구요청 테이블 해당 값 삭제. 친구 테이블에 각각 쌍방 친구관계 성립.
		// 트랜잭션을 처리하는 이유는 만약 친구요청 테이블을 삭제가 실패 할 경우 친구 테이블 삽입도 같이 rollback해야 함.
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
        	//쌍방 관계 성립. //친구 테이블 등록
        	friend.setMember1Id(toMemberId);
        	friend.setMember2Id(fromMemberId);
        	log.debug(friend);
        	dao.newRelationship(friend);
        	friend.setMember1Id(fromMemberId);
        	friend.setMember2Id(toMemberId);
        	dao.newRelationship(friend);
        	// 친구 요청 테이블 삭제.
        	dao.deleteFriendRequeset(result.getFriendRequestId());
        }catch (Exception e) {
			transactionManager.rollback(txStatus);
			return false;
		}
        
        transactionManager.commit(txStatus);
        return true;
	}

}

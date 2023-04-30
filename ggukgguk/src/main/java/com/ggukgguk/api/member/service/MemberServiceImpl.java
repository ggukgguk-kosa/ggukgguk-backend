package com.ggukgguk.api.member.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.util.MapUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.impl.MementoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import com.ggukgguk.api.common.vo.PageOption;
import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.member.controller.friendController;
import com.ggukgguk.api.member.dao.MemberDao;
import com.ggukgguk.api.member.vo.Friend;
import com.ggukgguk.api.member.vo.FriendRequest;
import com.ggukgguk.api.member.vo.Member;
import com.ggukgguk.api.notification.dao.NotificationDao;
import com.ggukgguk.api.notification.vo.Notification;
import com.nimbusds.oauth2.sdk.util.MapUtils;

import oracle.net.aso.f;

@Service
public class MemberServiceImpl implements MemberService {

	private Logger log = LogManager.getLogger("base");

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private MemberDao dao;

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private PasswordEncoder passwordEncorder;

	@Override
	// 아이디 중복검사 // 회원 정보 조회
	public Member findMemberById(String memberId) {
		return dao.selectMemberById(memberId);
	}

	// 회원가입
	@Override
	public boolean enrollMember(Member member) {
		try {
			member.setMemberPw(passwordEncorder.encode(member.getMemberPw()));
			dao.insertMember(member);
			return true;
		} catch (Exception e) {
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
		if (!user.equals(null))
			return true;
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
	public TotalAndListPayload getMemberList(PageOption option) {
		TotalAndListPayload payload = new TotalAndListPayload();
		payload.setList(dao.selectMemberList(option)); // 전체 회원 리스트 조회
		payload.setTotal(dao.selectMemberListTotal(option)); // 페이징 처리를 위한 전체회원 수 구하기
		return payload;
	}

	// 친구 요청
	@Override
	public boolean requestFriend(FriendRequest request) {
		// 친구 요청을 하면 알림도 같이 전송되어야 하기에 트랜잭션 처리를 해야.. 하지 않을 까 싶음.
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			// 친구 요청
			dao.requestFriend(request);

			// 위애서 친구요청한 테이블의 아이디 값을 가져오기
			int friendRequestId = (int) request.getFriendRequestId();

			// 친구 요청 알림 생성.
			Notification noti = new Notification(0, "FRIEND_REQUEST", new Date(), friendRequestId,
					request.getToMemberId(), 0, "친구 요청을 하였습니다.");
			// 알림 순번, 알림 타입 = "친구 요청". 알림 날짜 , 참조 아이디 = "방금 친구 요청 테이블 아이디", 수신자 = "전달받는
			// 아이디", 수신 여부 = 0, 전달 메시지
			notificationDao.createNotification(noti);

		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			e.printStackTrace();
			return false;
		}

		transactionManager.commit(txStatus);
		return true;
	}

	// 친구 수락
	@Override
	public boolean acceptFriend(Friend friend, FriendRequest friendRequest, String toMemberId, String fromMemberId) {
		// 먼저 친구 요청 테이블에 현재 관계가 있는 지 여부 조회
		friendRequest.setFromMemberId(toMemberId); // ex) (친구 요청을 한 사람 ) 손흥민
		friendRequest.setToMemberId(fromMemberId); // (ex) (친구를 요청 받은 사람) 홍길동
		log.debug(toMemberId);
		log.debug(friendRequest);
		FriendRequest result = dao.selectFriendRequestList(friendRequest);
		if (result.equals(null))
			return false;
		// 이후 트랜잭션 처리로 친구요청 테이블 해당 값 삭제. 친구 테이블에 각각 쌍방 친구관계 성립.
		// 트랜잭션을 처리하는 이유는 만약 친구요청 테이블을 삭제가 실패 할 경우 친구 테이블 삽입도 같이 rollback해야 함.
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			// 쌍방 관계 성립. //친구 테이블 등록
			friend.setMember1Id(toMemberId);
			friend.setMember2Id(fromMemberId);
			log.debug(friend);
			dao.newRelationship(friend);
			friend.setMember1Id(fromMemberId);
			friend.setMember2Id(toMemberId);
			dao.newRelationship(friend);
			// 친구 요청 테이블 삭제.
			dao.deleteFriendRequeset(result.getFriendRequestId());

		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			return false;
		}

		transactionManager.commit(txStatus);
		return true;
	}

	// 친구 찾기
	@Override
	public List<Member> findmyFriend(String memberId) {
		return dao.selectFindPartOfId(memberId);
	}

	// 현재 친구 목록 조회
	@Override
	public List<Member> findFriendList(String mymemberId) {
		try {
			List<Member> result = dao.selectFriendList(mymemberId);
			log.debug(result);
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	// 친구 차단
	@Override
	public boolean breakFriend(String myMemberId, String toMemberid, Friend friend) {
		// 트랜잭션 처리. 양방향 관계를 다 차단하여야 한다.
		TransactionStatus txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			// 양방향 관계 차단.
			friend.setMember1Id(myMemberId);
			friend.setMember2Id(toMemberid);
			log.debug(friend);
			dao.breakRelationship(friend);
			friend.setMember1Id(toMemberid);
			friend.setMember2Id(myMemberId);
			log.debug(friend);
			dao.breakRelationship(friend);
		} catch (Exception e) {
			transactionManager.rollback(txStatus);
			return false;
		}
		transactionManager.commit(txStatus);
		return true;
	}

	// 아이디 중복조회
	@Override
	public boolean checkDuplicateId(String memberId) {
		try {
			Member member = dao.selectMemberById(memberId);
			return member != null;
		} catch (NullPointerException e) {
			return false;
		}
	}
}

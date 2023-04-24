package com.ggukgguk.api.member.dao;

import java.util.List;

import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.member.vo.Friend;
import com.ggukgguk.api.member.vo.FriendRequest;
import com.ggukgguk.api.member.vo.Member;

public interface MemberDao {

	public Member selectMemberById(String memberId);

	public void insertMember(Member member) throws Exception;

	public Member selectMemberByEmail(String memberEmail);

	public Member selectMemberByEmailandId(Member member);

	public void updateMemberInfo(Member member) throws Exception;

	public List<?> selectMemberList(NoticeOption option);

	public int selectMemberListTotal(NoticeOption option);

	public void requestFriend(FriendRequest request) throws Exception;

	public void newRelationship(Friend friend) throws Exception;

	public FriendRequest selectFriendRequestList(FriendRequest friendRequest);
	
	public void deleteFriendRequeset(String friendRequestId) throws Exception;
	
	public List<Member> selectFindPartOfId(String memberId);

	public List<Member> selectFriendList(String mymemberId);

	public void breakRelationship(Friend friend) throws Exception;

	
}

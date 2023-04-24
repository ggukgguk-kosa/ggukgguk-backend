package com.ggukgguk.api.member.service;

import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.common.vo.TotalAndListPayload;
import com.ggukgguk.api.member.vo.Friend;
import com.ggukgguk.api.member.vo.FriendRequest;
import com.ggukgguk.api.member.vo.Member;

public interface MemberService {

	public Member findMemberById(String memberId);

	public boolean enrollMember(Member member);

	public Member getMemberByEmail(String memberEmail);

	public boolean getMemberByEmailandId(Member member);

	public boolean modifyMember(Member member);

	public TotalAndListPayload getMemberList(NoticeOption option);

	public boolean requestFriend(FriendRequest request);

	public boolean acceptFriend(Friend friend, FriendRequest friendRequest, String fromMemberId, String toMemberId);
}

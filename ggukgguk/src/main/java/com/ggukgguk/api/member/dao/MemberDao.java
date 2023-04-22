package com.ggukgguk.api.member.dao;

import com.ggukgguk.api.member.vo.Member;

public interface MemberDao {

	public Member selectMemberById(String memberId);

	public void insertMember(Member member) throws Exception;

	public Member selectMemberByEmail(String memberEmail);

	public Member selectMemberByEmailandId(Member member);
}

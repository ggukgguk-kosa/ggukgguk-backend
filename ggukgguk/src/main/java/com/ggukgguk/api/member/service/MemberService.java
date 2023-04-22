package com.ggukgguk.api.member.service;

import com.ggukgguk.api.member.vo.Member;

public interface MemberService {

	public Member findMemberById(String memberId);

	public boolean enrollMember(Member member);

	public Member getMemberByEmail(String memberEmail);

	public Boolean getMemberByEmailandId(Member member);
}

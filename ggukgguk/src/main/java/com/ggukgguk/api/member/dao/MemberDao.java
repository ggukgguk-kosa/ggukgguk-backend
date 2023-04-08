package com.ggukgguk.api.member.dao;

import com.ggukgguk.api.member.vo.Member;

public interface MemberDao {

	public Member selectMemberById(String memberId);
}

package com.ggukgguk.api.test.dao;

import com.ggukgguk.api.test.vo.Member;

public interface MemberDao {

	public Member selectMemberById(String memberId);
}

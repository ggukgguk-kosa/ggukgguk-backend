package com.ggukgguk.main.test.dao;

import com.ggukgguk.main.test.vo.Member;

public interface MemberDao {

	public Member selectMemberById(String memberId);
}

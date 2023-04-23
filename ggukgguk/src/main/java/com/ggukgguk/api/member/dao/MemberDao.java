package com.ggukgguk.api.member.dao;

import java.util.List;

import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.member.vo.Member;

public interface MemberDao {

	public Member selectMemberById(String memberId);

	public void insertMember(Member member) throws Exception;

	public Member selectMemberByEmail(String memberEmail);

	public Member selectMemberByEmailandId(Member member);

	public void updateMemberInfo(Member member) throws Exception;

	public List<?> selectMemberList(NoticeOption option);

	public int selectMemberListTotal(NoticeOption option);
}

package com.ggukgguk.api.admin.dao;

import java.util.List;

import com.ggukgguk.api.admin.vo.Content;
import com.ggukgguk.api.admin.vo.Member;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.PageOption;

public interface AdminDao {
    public void noticeInsert(Notice notice) throws Exception;

	public void noticeSelect(int noticeId)throws Exception;

	public void updateNotice(int noticeId) throws Exception;

	public void deleteNotice(int noticeId) throws Exception;

	public List<Notice> noticeSelectPaging(PageOption option);

	public List<Content> contentSelectPaging(PageOption option);
	
	public List<Member> memberSelectPaging(PageOption option);

	public void memberDelete(String memberId) throws Exception;
}


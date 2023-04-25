package com.ggukgguk.api.admin.dao;

import java.util.List;
import java.util.Map;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.admin.vo.NoticeOption;

public interface AdminDao {
    public void insertNotice(Notice notice) throws Exception;

	public void readNotice(int noticeId)throws Exception;

	public void updateNotice(int noticeId) throws Exception;

	public void deleteNotice(int noticeId) throws Exception;

	public List<Notice> pagingInsertBoard(NoticeOption option);

	public int totalCount();
}


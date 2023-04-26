package com.ggukgguk.api.admin.dao;

import java.util.List;
import java.util.Map;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.PageOption;

public interface AdminDao {
    public void insertNotice(Notice notice) throws Exception;

	public void readNotice(int noticeId)throws Exception;

	public void updateNotice(int noticeId) throws Exception;

	public void deleteNotice(int noticeId) throws Exception;

	public List<Notice> pagingInsertBoard(PageOption option);

	public int totalCount();

	public List<Notice> selectMediaList(PageOption option);
}


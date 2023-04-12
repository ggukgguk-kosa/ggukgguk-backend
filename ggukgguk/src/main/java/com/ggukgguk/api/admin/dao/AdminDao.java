package com.ggukgguk.api.admin.dao;

import java.util.List;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.admin.vo.NoticeOption;

public interface AdminDao {

	public void insertNotice(Notice notice) throws Exception;
	public List<Notice> selectNoticeList(NoticeOption option);
	public int selectNoticeListTotal(NoticeOption option);
}

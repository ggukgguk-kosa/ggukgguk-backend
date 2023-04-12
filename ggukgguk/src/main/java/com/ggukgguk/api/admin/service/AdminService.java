package com.ggukgguk.api.admin.service;

import java.util.List;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.admin.vo.NoticeOption;

public interface AdminService {

	public boolean addNotice(Notice notice);
	public List<Notice> getNoticeList(NoticeOption option);
}

package com.ggukgguk.api.admin.service;

import java.util.List;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.PageOption;

public interface AdminService {

    public boolean addNotice(Notice notice);

	public boolean readNotice(int noticeId);

	public boolean updateNotice(int noticeId);

	public boolean deleteNotice(int noticeId);

	
	public List<Notice> getListPaging(PageOption option);

	public int getNoticeCount();

	public List<Notice> getMediaList(PageOption option);
	
	
}

package com.ggukgguk.api.admin.service;

import java.util.List;

import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.admin.vo.NoticeOption;
import com.ggukgguk.api.common.vo.TotalAndListPayload;

public interface AdminService {

	public boolean addNotice(Notice notice);
	public TotalAndListPayload getNoticeList(NoticeOption option);
}

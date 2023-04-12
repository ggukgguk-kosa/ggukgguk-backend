package com.ggukgguk.api.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.admin.dao.AdminDao;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.admin.vo.NoticeOption;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDao dao;
	
	@Override
	public boolean addNotice(Notice notice) {
		try {
			dao.insertNotice(notice);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Notice> getNoticeList(NoticeOption option) {
		return dao.selectNoticeList(option);
	}

}

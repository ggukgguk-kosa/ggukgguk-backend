package com.ggukgguk.api.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.admin.dao.AdminDao;
import com.ggukgguk.api.admin.vo.Notice;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDao dao;
	
	@Override
	public void addNotice(Notice notice) {
		try {
			dao.insertNotice(notice);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

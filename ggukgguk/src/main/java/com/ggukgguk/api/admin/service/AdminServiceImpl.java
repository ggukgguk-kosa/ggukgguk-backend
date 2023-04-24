package com.ggukgguk.api.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggukgguk.api.admin.dao.AdminDao;
import com.ggukgguk.api.admin.vo.Notice;
import com.ggukgguk.api.common.vo.SearchCondition;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminDao adminDao;

	@Override
	public int getCount() throws Exception {
		return adminDao.count();
	}

	@Override
	public int remove(int noticeId, String writer) throws Exception {
		return adminDao.delete(noticeId, writer);
	}

	@Override
	public int write(Notice notice) throws Exception {
		return adminDao.insert(notice);
	}

	@Override
	public List<Notice> getList() throws Exception {
		return adminDao.selectAll();
	}

	@Override
	public Notice read(int noticeId) throws Exception {
		Notice notice = adminDao.select(noticeId);
		adminDao.increaseViewCnt(noticeId);

		return notice;
	}

	@Override
	public List<Notice> getPage(Map map) throws Exception {
		return adminDao.selectPage(map);
	}

	@Override
	public int modify(Notice notice) throws Exception {
		return adminDao.update(notice);
	}

//	@Override
//	public int getSearchResultCnt(SearchCondition sc) throws Exception {
//		return adminDao.searchResultCnt(sc);
//	}
//
//	@Override
//	public List<Notice> getSearchResultPage(SearchCondition sc) throws Exception {
//		return adminDao.searchSelectPage(sc);
//	}
}

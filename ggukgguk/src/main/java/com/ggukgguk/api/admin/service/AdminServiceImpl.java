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

    public boolean addNotice(Notice notice) {
        try {
            dao.insertNotice(notice);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean readNotice(int noticeId) {
        try {
            dao.readNotice(noticeId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateNotice(int noticeId) {
        try {
            dao.updateNotice(noticeId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteNotice(int noticeId) {
        try {
            dao.deleteNotice(noticeId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	@Override
	public List<Notice> getListPaging(NoticeOption option) {
		return dao.pagingInsertBoard(option);
	}
	
	@Override
	public int getNoticeCount() {
		return dao.totalCount();
	}

}
